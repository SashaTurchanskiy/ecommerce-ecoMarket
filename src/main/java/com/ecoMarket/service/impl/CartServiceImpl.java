package com.ecoMarket.service.impl;

import com.ecoMarket.dtos.request.CartItemsRequest;
import com.ecoMarket.dtos.request.UserRequest;
import com.ecoMarket.dtos.response.CartItemsResponse;
import com.ecoMarket.dtos.response.CartResponse;
import com.ecoMarket.mapper.CartItemsMapper;
import com.ecoMarket.mapper.CartMapper;
import com.ecoMarket.model.Cart;
import com.ecoMarket.model.CartItems;
import com.ecoMarket.model.Product;
import com.ecoMarket.model.User;
import com.ecoMarket.repository.CartItemRepository;
import com.ecoMarket.repository.CartRepository;
import com.ecoMarket.repository.ProductRepository;
import com.ecoMarket.repository.UserRepository;
import com.ecoMarket.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;
    private final CartItemsMapper cartItemsMapper;

    @Override
    @Transactional
    public CartItemsResponse addCartItem(
            UserRequest userRequest,
            CartItemsRequest cartItemsRequest
    ) {
        validateUserRequest(userRequest);
        validateCartItemsRequest(cartItemsRequest);

        User user = userRepository.findById(userRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Product product = productRepository.findById(cartItemsRequest.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Cart cart = getOrCreateCart(user);

        CartItems cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .filter(item -> java.util.Objects.equals(item.getSize(), cartItemsRequest.getSize()))
                .findFirst()
                .orElse(null);

        if (cartItem == null) {
            cartItem = cartItemsMapper.toEntity(cartItemsRequest);
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setUserId(user.getId());
            cart.getCartItems().add(cartItem);
        } else {
            int updatedQuantity = cartItem.getQuantity() + cartItemsRequest.getQuantity();
            cartItem.setQuantity(updatedQuantity);
        }

        if (cartItem.getQuantity() > product.getQuantity()) {
            throw new IllegalArgumentException("Not enough product quantity available");
        }

        cartItem.setMrpPrice(cartItem.getQuantity() * product.getMrpPrice());
        cartItem.setSellingPrice(cartItem.getQuantity() * product.getSellingPrice());

        CartItems savedCartItem = cartItemRepository.save(cartItem);
        recalculateCartTotals(cart);
        cartRepository.save(cart);

        return cartItemsMapper.toResponse(savedCartItem);
    }

    @Override
    @Transactional
    public CartResponse findUserCart(UserRequest request) {
        validateUserRequest(request);

        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Cart cart = getOrCreateCart(user);
        recalculateCartTotals(cart);
        cartRepository.save(cart);

        return cartMapper.toResponse(cart);
    }

    private Cart getOrCreateCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId());

        if (cart == null) {
            cart = Cart.builder()
                    .user(user)
                    .build();
            cart = cartRepository.save(cart);
        }

        return cart;
    }

    private void recalculateCartTotals(Cart cart) {
        int totalMrpPrice = 0;
        int totalSellingPrice = 0;
        int totalItems = 0;

        for (CartItems cartItem : cart.getCartItems()) {
            totalMrpPrice += cartItem.getMrpPrice();
            totalSellingPrice += cartItem.getSellingPrice();
            totalItems += cartItem.getQuantity();
        }

        cart.setTotalMrpPrice(totalMrpPrice);
        cart.setTotalSellingPrice(totalSellingPrice);
        cart.setTotalItem(totalItems);
        cart.setDiscount(calculateDiscountPercentage(totalMrpPrice, totalSellingPrice));
    }

    private void validateUserRequest(UserRequest request) {
        if (request == null || request.getId() == null) {
            throw new IllegalArgumentException("User id is required");
        }
    }

    private void validateCartItemsRequest(CartItemsRequest request) {
        if (request == null || request.getProductId() == null) {
            throw new IllegalArgumentException("Product id is required");
        }

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if (mrpPrice <= 0){
            return 0;
        }
        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount / mrpPrice) * 100;
        return (int) discountPercentage;

    }
}
