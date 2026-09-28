package com.ecoMarket.service.impl;

import com.ecoMarket.dtos.request.CartItemsRequest;
import com.ecoMarket.dtos.response.CartItemsResponse;
import com.ecoMarket.mapper.CartItemsMapper;
import com.ecoMarket.model.CartItems;
import com.ecoMarket.model.User;
import com.ecoMarket.repository.CartItemRepository;
import com.ecoMarket.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartItemsMapper cartItemsMapper;

    @Override
    public CartItemsResponse updateCartItem(Long userId, Long id, CartItemsRequest request) throws Exception {
        CartItems cartItems = cartItemRepository.findById(id)
                .orElseThrow(()-> new Exception("Cart item not found"));

        User cartItemUser = cartItems.getCart().getUser();

        if (cartItemUser.getId().equals(userId)){
            cartItems.setQuantity(cartItems.getQuantity());
            cartItems.setMrpPrice(cartItems.getQuantity() * cartItems.getProduct().getMrpPrice());
            cartItems.setSellingPrice(cartItems.getQuantity() * cartItems.getProduct().getSellingPrice());

            return cartItemsMapper.toResponse(cartItemRepository.save(cartItems));
        }

        throw new Exception("You cant update this cart item");
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws Exception {
        CartItems cartItems = cartItemRepository.findById(cartItemId)
                        .orElseThrow(()-> new Exception("Cart item not found"));

        if (!cartItems.getCart().getUser().getId().equals(userId)){
            throw new Exception("Access denied");
        }
        cartItemRepository.deleteById(cartItemId);

    }

    @Override
    public CartItemsResponse findCartItemById(Long id) throws Exception {
        CartItems cartItems = cartItemRepository.findById(id)
                .orElseThrow(()-> new Exception("Cart item not found"));
        return cartItemsMapper.toResponse(cartItems);
    }
}
