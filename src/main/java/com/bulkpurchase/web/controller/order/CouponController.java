package com.bulkpurchase.web.controller.order;

import com.bulkpurchase.domain.dto.CouponRequestDTO;
import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.coupon.CouponApplicableProduct;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.coupon.CouponService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.validator.coupon.CouponValidatorImpl;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import com.bulkpurchase.web.service.coupon.ApplyCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Controller
public class CouponController {

    private final UserAuthValidator userAuthValidator;
    private final ApplyCouponService applyCouponService;
    private final CouponService couponService;
    private final CouponValidatorImpl couponValidator;

    @PostMapping("/find/coupon")
    public ResponseEntity<List<Coupon>> findCoupon(@RequestBody CouponRequestDTO couponRequest,
                                         Principal principal) {
        Long productID = couponRequest.getProductID();
        Double totalPrice = couponRequest.getTotalPrice();
        User user = userAuthValidator.getCurrentUser(principal);
        List<Coupon> couponListByUser = couponService.findByUser(user);
        List<Coupon> couponList = new ArrayList<>();
        for (Coupon coupon : couponListByUser) {
            Set<CouponApplicableProduct> applicableProducts = coupon.getApplicableProducts();
            for (CouponApplicableProduct applicableProduct : applicableProducts) {
                Long applicableProductProductId = applicableProduct.getProductId();
                if (applicableProductProductId.equals(productID) && couponValidator.isItValidCoupon(user, coupon, productID, totalPrice)) {
                    couponList.add(coupon);
                    break;
                }
            }
        }
        return ResponseEntity.ok(couponList);
    }

    @PostMapping("/apply/coupon")
    public ResponseEntity<?> applyCoupon(@RequestParam("productID") Long productID,
                                         @RequestParam("couponID") Long couponID,
                                         @RequestParam("totalPrice") Double totalPrice,
                                         Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        Double applyPrice = applyCouponService.applyCoupon(user, couponID, productID, totalPrice);
        return ResponseEntity.ok(applyPrice);
    }
}
