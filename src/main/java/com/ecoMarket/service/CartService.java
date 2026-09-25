package com.ecoMarket.service;

import com.ecoMarket.dtos.request.CartItemsRequest;
import com.ecoMarket.dtos.request.UserRequest;
import com.ecoMarket.dtos.response.CartItemsResponse;
import com.ecoMarket.dtos.response.CartResponse;

public interface CartService {

    CartItemsResponse addCartItem(UserRequest userRequest, CartItemsRequest cartItemsRequest);

    CartResponse findUserCart(UserRequest request);

}
