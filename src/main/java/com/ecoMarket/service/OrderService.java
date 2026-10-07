package com.ecoMarket.service;

import com.ecoMarket.dtos.request.CartRequest;
import com.ecoMarket.dtos.request.UserRequest;
import com.ecoMarket.dtos.response.CartResponse;
import com.ecoMarket.dtos.response.OrderItemResponse;
import com.ecoMarket.dtos.response.OrderResponse;
import com.ecoMarket.model.Address;
import com.ecoMarket.model.Cart;
import com.ecoMarket.model.Order;
import com.ecoMarket.model.User;
import com.ecoMarket.model.enums.OrderStatus;

import java.util.List;
import java.util.Set;

public interface OrderService {

    Set<Order> createOrder(User user, Address shippingAddress, Cart cart);

    OrderResponse findByOrderId(Long id) throws Exception;

    List<OrderResponse> userOrderHistory(Long userId);

    List<OrderResponse> sellersOrder(Long sellerId);

    OrderResponse updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception;

    Order cancelOrder(Long orderId, User user) throws Exception;

    OrderItemResponse getOrderById(Long id) throws Exception;

}
