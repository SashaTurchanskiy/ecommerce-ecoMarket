package com.ecoMarket.service.impl;

import com.ecoMarket.model.Cart;
import com.ecoMarket.model.Coupon;
import com.ecoMarket.model.User;
import com.ecoMarket.repository.CouponRepository;
import com.ecoMarket.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    @Override
    public Cart applyCoupon(String couponCode, double orderValue, User user) {
        return null;
    }

    @Override
    public Cart removeCoupon(String code, User user) {
        return null;
    }

    @Override
    public Coupon findCouponById(Long id) {
        return null;
    }

    @Override
    public Coupon createCoupon(Coupon coupon) {
        return null;
    }

    @Override
    public List<Coupon> findAllCoupons() {
        return List.of();
    }

    @Override
    public void deleteCoupon(Long id) {

    }
}
