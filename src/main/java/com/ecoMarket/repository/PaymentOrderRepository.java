package com.ecoMarket.repository;

import com.ecoMarket.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {

    PaymentOrder findPaymentLinkId(String paymentId);
}
