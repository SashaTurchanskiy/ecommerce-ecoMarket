package com.ecoMarket.service.impl;

import com.ecoMarket.dtos.request.CartRequest;
import com.ecoMarket.dtos.request.CartItemsRequest;
import com.ecoMarket.dtos.request.UserRequest;
import com.ecoMarket.dtos.response.OrderItemResponse;
import com.ecoMarket.dtos.response.OrderResponse;
import com.ecoMarket.mapper.OrderItemMapper;
import com.ecoMarket.mapper.OrderMapper;
import com.ecoMarket.model.*;
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
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {
        if (!user.getAddresses().contains(shippingAddress)){
            user.getAddresses().add(shippingAddress);
        }
        Address address = addressRepository.save(shippingAddress);

        Map<Long, List<CartItems>> itemsBySeller = cart.getCartItems().stream()
                .collect(Collectors.groupingBy(item -> item.getProduct()
                        .getSeller().getId()));

        Set<Order> orders = new HashSet<>();

        for (Map.Entry<Long, List<CartItems>> entry : itemsBySeller.entrySet()) {
            Long sellerId = entry.getKey();

            List<CartItems> items = entry.getValue();

            int totalOrderPrice = items.stream().mapToInt(CartItems::getSellingPrice).sum();
            int totalItem = items.stream().mapToInt(CartItems::getQuantity).sum();

            Order createdOrder = new Order();
            createdOrder.setUser(user);
            createdOrder.setSellerId(sellerId);
            createdOrder.setTotalMrpPrice(totalOrderPrice);
            createdOrder.setTotalSellingPrice(totalOrderPrice);
            createdOrder.setTotalItem(totalItem);
            createdOrder.setShippingAddress(address);
            createdOrder.setOrderStatus(OrderStatus.PENDING);
            createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);

            Order savedOrder = orderRepository.save(createdOrder);
            orders.add(savedOrder);

            List<OrderItem> orderItems = new ArrayList<>();

            for (CartItems item : items){
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(savedOrder);
                orderItem.setMrpPrice(item.getMrpPrice());
                orderItem.setProduct(item.getProduct());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setSize(item.getSize());
                orderItem.setUserId(item.getUserId());
                orderItem.setSellingPrice(item.getSellingPrice());

                savedOrder.getOrderItems().add(orderItem);
                OrderItem savedOrderItem = orderItemRepository.save(orderItem);
                orderItems.add(savedOrderItem);
            }
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
    public Order cancelOrder(Long orderId, User user) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found with id " + orderId));

        if (!user.getId().equals(order.getUser().getId())){
            throw new Exception("You dont have permission to cancel this order");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        //Order savedOrder = orderRepository.save(order);
        return orderRepository.save(order);
    }

    @Override
    public OrderItemResponse getOrderById(Long id) throws Exception {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(()-> new Exception("Order item not exist..."));
        return orderItemMapper.toResponse(orderItem);
    }
}
