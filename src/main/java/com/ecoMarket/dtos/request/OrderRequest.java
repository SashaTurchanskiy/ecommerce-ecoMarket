package com.ecoMarket.dtos.request;

import com.ecoMarket.model.Address;
import com.ecoMarket.model.OrderItem;
import com.ecoMarket.model.PaymentDetails;
import com.ecoMarket.model.User;
import com.ecoMarket.model.enums.OrderStatus;
import com.ecoMarket.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {

    private String orderId;
    private User user;
    private Long sellerId;
    private List<OrderItem> orderItems;
    private Address shippingAddress;
    private PaymentDetails paymentDetails;
    private double totalMrpPrice;
    private Integer totalSellingPrice;
    private Integer discount;
    private OrderStatus orderStatus;
    private int totalItem;
    private PaymentStatus paymentStatus;
    private LocalDateTime orderDate;
    private LocalDateTime deliverDate;
}
