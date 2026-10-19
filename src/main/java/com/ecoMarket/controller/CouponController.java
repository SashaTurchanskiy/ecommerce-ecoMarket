package com.ecoMarket.controller;

import com.ecoMarket.model.Cart;
import com.ecoMarket.model.Coupon;
import com.ecoMarket.model.User;
import com.ecoMarket.service.CouponService;
import com.ecoMarket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private final UserService userService;

    @PostMapping("/apply")
    public ResponseEntity<Cart> applyCoupon(
            @RequestParam String couponCode,
            @RequestParam String apply,
            @RequestParam double orderValue,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Cart cart;
        if (apply.equals("true")){
            cart = couponService.applyCoupon(couponCode, orderValue, user);
        } else {
            cart = couponService.removeCoupon(couponCode, user);
        }
        return ResponseEntity.ok(cart);
    }

    //admin operations
    @PostMapping("/create")
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon){
        Coupon createdCoupon = couponService.createCoupon(coupon);
        return ResponseEntity.ok(createdCoupon);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCoupon(@PathVariable Long id) throws Exception {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok("Coupon deleted successfully");
    }
    @GetMapping("/all")
    public ResponseEntity<List<Coupon>> getAllCoupons(){
        List<Coupon> coupons = couponService.findAllCoupons();
        return ResponseEntity.ok(coupons);
    }

}
