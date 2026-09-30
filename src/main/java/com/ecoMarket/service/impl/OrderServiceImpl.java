package com.ecoMarket.service.impl;

import com.ecoMarket.dtos.request.CartRequest;
import com.ecoMarket.dtos.request.CartItemsRequest;
import com.ecoMarket.dtos.request.UserRequest;
import com.ecoMarket.dtos.response.OrderItemResponse;
import com.ecoMarket.dtos.response.OrderResponse;
import com.ecoMarket.mapper.OrderItemMapper;
import com.ecoMarket.mapper.OrderMapper;
import com.ecoMarket.model.Address;
import com.ecoMarket.model.Order;
import com.ecoMarket.model.OrderItem;
import com.ecoMarket.model.PaymentDetails;
import com.ecoMarket.model.Product;
import com.ecoMarket.model.User;
import com.ecoMarket.model.enums.OrderStatus;
import com.ecoMarket.model.enums.PaymentStatus;
import com.ecoMarket.repository.AddressRepository;
import com.ecoMarket.repository.OrderItemRepository;
import com.ecoMarket.repository.OrderRepository;
import com.ecoMarket.repository.ProductRepository;
import com.ecoMarket.repository.UserRepository;
import com.ecoMarket.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public Set<OrderResponse> createOrder(UserRequest request, Address shippingAddress, CartRequest cartRequest) {
        if (request == null || request.getId() == null) {
            throw new IllegalArgumentException("User id is required");
        }
        if (shippingAddress == null) {
            throw new IllegalArgumentException("Shipping address is required");
        }
        if (cartRequest == null || cartRequest.getCartItems() == null
                || cartRequest.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart must contain at least one item");
        }

        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + request.getId()));

        if (user.getAddresses() == null) {
            user.setAddresses(new HashSet<>());
        }
        shippingAddress.setUsers(user);
        Address savedAddress = addressRepository.save(shippingAddress);
        boolean addressAlreadyAttached = user.getAddresses().stream()
                .anyMatch(address -> savedAddress.getId() != null
                        && savedAddress.getId().equals(address.getId()));
        if (!addressAlreadyAttached) {
            user.getAddresses().add(savedAddress);
        }
        userRepository.save(user);

        List<CartItemsRequest> cartItems = new ArrayList<>(cartRequest.getCartItems());
        Map<Long, Integer> quantityByProduct = new HashMap<>();
        for (CartItemsRequest cartItem : cartItems) {
            if (cartItem == null || cartItem.getProductId() == null) {
                throw new IllegalArgumentException("Every cart item must have a product id");
            }
            if (cartItem.getQuantity() == null || cartItem.getQuantity() <= 0) {
                throw new IllegalArgumentException("Cart item quantity must be greater than zero");
            }
            quantityByProduct.merge(cartItem.getProductId(), cartItem.getQuantity(), Math::addExact);
        }

        Map<Long, Product> productsById = productRepository.findAllById(cartItems.stream()
                        .map(CartItemsRequest::getProductId)
                        .collect(Collectors.toSet()))
                .stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        Map<Long, List<OrderItem>> itemsBySeller = new HashMap<>();
        for (CartItemsRequest cartItem : cartItems) {
            Product product = productsById.get(cartItem.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("Product not found with id " + cartItem.getProductId());
            }
            if (product.getSeller() == null || product.getSeller().getId() == null) {
                throw new IllegalArgumentException("Product has no seller: " + product.getId());
            }
            if (quantityByProduct.get(product.getId()) > product.getQuantity()) {
                throw new IllegalArgumentException("Not enough product quantity available: " + product.getId());
            }

            OrderItem item = OrderItem.builder()
                    .product(product)
                    .size(cartItem.getSize())
                    .quantity(cartItem.getQuantity())
                    .mrpPrice(Math.multiplyExact(product.getMrpPrice(), cartItem.getQuantity()))
                    .sellingPrice(Math.multiplyExact(product.getSellingPrice(), cartItem.getQuantity()))
                    .userId(user.getId())
                    .build();
            itemsBySeller.computeIfAbsent(product.getSeller().getId(), ignored -> new ArrayList<>()).add(item);
        }

        Set<OrderResponse> orders = Collections.newSetFromMap(new IdentityHashMap<>());
        for (Map.Entry<Long, List<OrderItem>> entry : itemsBySeller.entrySet()) {
            List<OrderItem> items = entry.getValue();
            int totalMrpPrice = items.stream().mapToInt(OrderItem::getMrpPrice).reduce(0, Math::addExact);
            int totalSellingPrice = items.stream().mapToInt(OrderItem::getSellingPrice).reduce(0, Math::addExact);
            int totalItems = items.stream().mapToInt(OrderItem::getQuantity).reduce(0, Math::addExact);

            Order order = new Order();
            order.setUser(user);
            order.setSellerId(entry.getKey());
            order.setShippingAddress(savedAddress);
            order.setOrderStatus(OrderStatus.PENDING);
            order.setPaymentStatus(PaymentStatus.PENDING);
            PaymentDetails paymentDetails = new PaymentDetails();
            paymentDetails.setStatus(PaymentStatus.PENDING);
            order.setPaymentDetails(paymentDetails);
            order.setTotalMrpPrice(totalMrpPrice);
            order.setTotalSellingPrice(totalSellingPrice);
            order.setTotalItem(totalItems);
            order.setDiscount(totalMrpPrice == 0
                    ? 0
                    : (int) (((double) (totalMrpPrice - totalSellingPrice) / totalMrpPrice) * 100));

            for (OrderItem item : items) {
                item.setOrder(order);
                order.getOrderItems().add(item);
            }

            orders.add(orderMapper.toResponse(orderRepository.save(order)));
        }
        return orders;
    }

    @Override
    public OrderResponse findByOrderId(Long id) throws Exception {
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new Exception("Order not found with " + id));
        return orderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> userOrderHistory(Long userId) {
        return orderMapper.toOrderList(orderRepository.findByUserId(userId));
    }

    @Override
    public List<OrderResponse> sellersOrder(Long sellerId) {
        return orderMapper.toOrderList(orderRepository.findBySellerId(sellerId));
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new Exception("Order not found with " + orderId));
        order.setOrderStatus(orderStatus);
        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    public OrderResponse cancelOrder(Long orderId, UserRequest request) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found with id " + orderId));

        if (!request.getId().equals(order.getUser().getId())){
            throw new Exception("You dont have permission to cancel this order");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        //Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    public OrderItemResponse getOrderById(Long id) throws Exception {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(()-> new Exception("Order item not exist..."));
        return orderItemMapper.toResponse(orderItem);
    }
}
