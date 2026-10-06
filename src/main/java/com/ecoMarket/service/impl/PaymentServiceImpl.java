package com.ecoMarket.service.impl;

import com.ecoMarket.model.Order;
import com.ecoMarket.model.PaymentOrder;
import com.ecoMarket.model.User;
import com.ecoMarket.repository.OrderRepository;
import com.ecoMarket.repository.PaymentOrderRepository;
import com.ecoMarket.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final OrderRepository orderRepository;

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
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) {
        return null;
    }

    @Override
    public String createStripePaymentLink(User user, Long amount, Long orderId) {
        return "";
    }
}
