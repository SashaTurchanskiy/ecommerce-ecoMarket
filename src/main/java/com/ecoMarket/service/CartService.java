package com.ecoMarket.service;

import com.ecoMarket.dtos.request.CartItemsRequest;
import com.ecoMarket.dtos.response.CartItemsResponse;
import com.ecoMarket.dtos.response.CartResponse;

public interface CartService {

    CartItemsResponse addCartItem(Long userId, CartItemsRequest cartItemsRequest);

    CartResponse findUserCart(Long userId);

}
