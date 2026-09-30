package com.ecoMarket.service;

import com.ecoMarket.dtos.request.CartRequest;
import com.ecoMarket.dtos.request.UserRequest;
import com.ecoMarket.dtos.response.CartResponse;
import com.ecoMarket.dtos.response.OrderItemResponse;
import com.ecoMarket.dtos.response.OrderResponse;
import com.ecoMarket.model.Address;
import com.ecoMarket.model.Order;
import com.ecoMarket.model.enums.OrderStatus;

import java.util.List;
import java.util.Set;

public interface OrderService {

    Set<OrderResponse> createOrder(UserRequest request, Address shippingAddress, CartRequest cartRequest);

    OrderResponse findByOrderId(Long id) throws Exception;

    List<OrderResponse> userOrderHistory(Long userId);

    List<OrderResponse> sellersOrder(Long sellerId);

    OrderResponse updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception;

    OrderResponse cancelOrder(Long orderId, UserRequest request) throws Exception;

    OrderItemResponse getOrderById(Long id) throws Exception;

}
