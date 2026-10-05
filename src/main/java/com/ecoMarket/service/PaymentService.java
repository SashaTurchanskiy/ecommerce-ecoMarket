package com.ecoMarket.service;

import com.ecoMarket.model.Order;
import com.ecoMarket.model.PaymentOrder;
import com.ecoMarket.model.User;

import java.util.Set;

public interface PaymentService {

    PaymentOrder createOrder(User user, Set<Order> orders);

    PaymentOrder getPaymentOrderById(Long orderId);

    PaymentOrder getPaymentOrderByPaymentId(String orderId);

    Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId);

    String createStripePaymentLink(User user, Long amount, Long orderId);
}
