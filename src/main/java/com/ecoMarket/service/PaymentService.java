package com.ecoMarket.service;

import com.ecoMarket.model.Order;
import com.ecoMarket.model.PaymentOrder;
import com.ecoMarket.model.User;
import com.stripe.exception.StripeException;

import java.util.Set;

public interface PaymentService {

    PaymentOrder createOrder(User user, Set<Order> orders);

    PaymentOrder getPaymentOrderById(Long orderId) throws Exception;

    PaymentOrder getPaymentOrderByPaymentId(String orderId) throws Exception;

    Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws StripeException;

    String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException;
}
