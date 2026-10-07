package com.ecoMarket.service;

import com.ecoMarket.dtos.request.CartItemsRequest;
import com.ecoMarket.dtos.response.CartItemsResponse;
import com.ecoMarket.dtos.response.CartResponse;
import com.ecoMarket.model.Cart;
import com.ecoMarket.model.User;

public interface CartService {

    CartItemsResponse addCartItem(Long userId, CartItemsRequest cartItemsRequest);

    Cart findUserCart(User user);

}
