package com.ecoMarket.service.impl;

import com.ecoMarket.model.Cart;
import com.ecoMarket.model.Coupon;
import com.ecoMarket.model.User;
import com.ecoMarket.repository.CartRepository;
import com.ecoMarket.repository.CouponRepository;
import com.ecoMarket.repository.UserRepository;
import com.ecoMarket.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Override
    public Cart applyCoupon(String couponCode, double orderValue, User user) throws Exception {
        Coupon coupon = couponRepository.findByCode(couponCode);
        Cart cart = cartRepository.findByUserId(user.getId());

        if (coupon == null){
            throw new Exception("Coupon not found with code: " + couponCode);
        }
        if (user.getUsedCoupons().contains(coupon)){
            throw new Exception("Coupon already used by the user: " + couponCode);
        }
        if (orderValue < coupon.getMinimumOrderValue()){
            throw new Exception("Order value is less than the minimum required for this coupon: " + couponCode);
        }
        if (coupon.isActive() && LocalDate.now().isAfter(coupon.getValidityStartDate()) && LocalDate.now().isBefore(coupon.getValidityEndDate())){
            user.getUsedCoupons().add(coupon);
            userRepository.save(user);

            double discountPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage()) / 100;

            cart.setTotalSellingPrice(cart.getTotalSellingPrice() - discountPrice);
            cart.setCouponCode(couponCode);
            cartRepository.save(cart);

            return cart;
        }
        throw new Exception("Coupon is not valid or expired: " + couponCode);
    }

    @Override
    public Cart removeCoupon(String code, User user) throws Exception {
        Coupon coupon = couponRepository.findByCode(code);

        if (coupon == null){
            throw new Exception("Coupon not found with code: " + code);
        }

        Cart cart = cartRepository.findByUserId(user.getId());
        double discountPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage()) / 100;

        cart.setTotalSellingPrice(cart.getTotalSellingPrice() + discountPrice);
        cart.setCouponCode(null);
        return cartRepository.save(cart);
    }

    @Override
    public Coupon findCouponById(Long id) throws Exception {
        return couponRepository.findById(id)
                .orElseThrow(()-> new Exception("Coupon not found with id: " + id));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);

    }

    @Override
    public List<Coupon> findAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCoupon(Long id) throws Exception {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(()-> new Exception("Coupon not found with id: " + id));
        couponRepository.delete(coupon);

    }
}
