package com.bulkpurchase.web.controller.seller;

import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.coupon.CouponApplicableProduct;
import com.bulkpurchase.domain.service.coupon.CouponApplicableProductService;
import com.bulkpurchase.domain.service.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/seller/coupon")
@RequiredArgsConstructor
public class SellerCouponManageController {

    private final CouponService couponService;
    private final CouponApplicableProductService couponApplicableProductService;

    @PostMapping("/select")
    @Transactional
    public String selectProductForCoupon(@RequestParam("couponID") Long couponID,
                                         @RequestParam(value = "productIDs", required = false) List<Long> productIDs) {
        Coupon coupon = couponService.findById(couponID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));

        couponApplicableProductService.deleteByCoupon(coupon);

        if (productIDs != null) {
            productIDs.forEach(productID -> {
                CouponApplicableProduct cap = new CouponApplicableProduct(coupon, productID);
                couponApplicableProductService.save(cap);
            });
        }

        return "redirect:/seller/coupon/list";
    }
}
