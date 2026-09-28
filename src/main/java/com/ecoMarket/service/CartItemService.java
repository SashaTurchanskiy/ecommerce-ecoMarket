package com.ecoMarket.service;

import com.ecoMarket.dtos.request.CartItemsRequest;
import com.ecoMarket.dtos.response.CartItemsResponse;

public interface CartItemService {

    CartItemsResponse updateCartItem(Long userId, Long id, CartItemsRequest request) throws Exception;

    void removeCartItem(Long userId, Long cartItemId) throws Exception;

    CartItemsResponse findCartItemById(Long id) throws Exception;
}
