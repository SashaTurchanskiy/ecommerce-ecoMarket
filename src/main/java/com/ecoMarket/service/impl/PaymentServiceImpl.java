package com.ecoMarket.service.impl;

import com.ecoMarket.model.Order;
import com.ecoMarket.model.PaymentOrder;
import com.ecoMarket.model.User;
import com.ecoMarket.model.enums.PaymentOrderStatus;
import com.ecoMarket.model.enums.PaymentStatus;
import com.ecoMarket.repository.OrderRepository;
import com.ecoMarket.repository.PaymentOrderRepository;
import com.ecoMarket.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final OrderRepository orderRepository;

    @Value("${stripe.api.key}")
    private String apiKey;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Override
    public PaymentOrder createOrder(User user, Set<Order> orders) {
        Long amount = orders.stream().mapToLong(Order::getTotalSellingPrice).sum();

        PaymentOrder po = new PaymentOrder();
        po.setUser(user);
        po.setOrders(orders);
        po.setAmount(amount);

        return paymentOrderRepository.save(po);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long orderId) throws Exception {
        return paymentOrderRepository.findById(orderId)
                .orElseThrow(()-> new Exception("Payment order not found with id: " + orderId));
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String orderId) throws Exception {
        PaymentOrder po = paymentOrderRepository.findPaymentLinkId(orderId);

        if (po == null){
            throw new Exception("Payment order not found with payment id: " + orderId);
        }
        return po;
    }

    @Override
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws StripeException {
        if (paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){
            Stripe.apiKey = stripeSecretKey;

            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentId);

            if ("succeded".equals(paymentIntent.getStatus())){
                Set<Order> orders = paymentOrder.getOrders();
                for (Order order : orders){
                    order.setPaymentStatus(PaymentStatus.COMPLETED);
                    orderRepository.save(order);
                }
                paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                paymentOrderRepository.save(paymentOrder);
                return true;
            } else {
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                paymentOrderRepository.save(paymentOrder);
                return false;
            }

        }
        return false;
    }

    @Override
    public String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addAllowedPaymentMethodType(SessionCreateParams.AllowedPaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/payment/success?orderId=" + orderId)
                .setCancelUrl("http://localhost:8080/payment/cancel?orderId=" + orderId)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(amount)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Order #" + orderId)
                                        .build()
                                ).build()
                        ).build()
                ).build();
        Session session = Session.create(params);
        return session.getUrl();
    }
}
