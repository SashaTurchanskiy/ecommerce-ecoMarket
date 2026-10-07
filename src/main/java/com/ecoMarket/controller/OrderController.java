package com.ecoMarket.controller;

import com.ecoMarket.dtos.response.*;
import com.ecoMarket.mapper.CartMapper;
import com.ecoMarket.mapper.UserMapper;
import com.ecoMarket.model.*;
import com.ecoMarket.repository.PaymentOrderRepository;
import com.ecoMarket.service.CartService;
import com.ecoMarket.service.OrderService;
import com.ecoMarket.service.PaymentService;
import com.ecoMarket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;
    private final CartMapper cartMapper;
    private final UserMapper userMapper;
    private final PaymentService paymentService;
    private final PaymentOrderRepository paymentOrderRepository;

    @PostMapping("/create")
    public ResponseEntity<Set<Order>> createOrderHandle(
            @RequestBody Address shippingAddress,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);
        Set<Order> orders = orderService.createOrder(user, shippingAddress, cart);

        PaymentOrder paymentOrder = paymentService.createOrder(user, orders);

        PaymentLinkResponse res = new PaymentLinkResponse();

        String paymentUrl = paymentService.createStripePaymentLink(user,
                paymentOrder.getAmount(),
                paymentOrder.getId());

        res.setPayment_link_url(paymentUrl);

        paymentOrder.setPaymentLinkId(paymentUrl);
        paymentOrderRepository.save(paymentOrder);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user")
    public ResponseEntity<List<OrderResponse>> userOrderHistoryHandler(
            @RequestHeader ("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        List<OrderResponse> orders = orderService.userOrderHistory(user.getId());
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long orderId,
            @RequestHeader ("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        OrderResponse orders = orderService.findByOrderId(orderId);
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @GetMapping("/items/{orderItemId}")
    public ResponseEntity<OrderItemResponse> getOrderItemById(
            @PathVariable Long orderItemId,
            @RequestHeader ("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        OrderItemResponse orderItem = orderService.getOrderById(orderItemId);
        return new ResponseEntity<>(orderItem, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable Long orderId,
            @RequestHeader ("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Order order = orderService.cancelOrder(orderId, user);

//        Seller seller = sellerService.getSellerById(order.getSellerId());
//        SellerReport report = sellerReportService.getSellerReport(seller);
//
//        report.setCanceledOrders(report.getCanceledOrders() + 1);
//        report.setTotalRefunds(report.getTotalRefunds() + order.getTotalSellingPrice());
//        sellerReportService.updateSellerReport(report);

        return ResponseEntity.ok(order);
    }
}
