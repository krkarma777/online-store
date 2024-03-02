package com.bulkpurchase.web.service.coupon;

import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.CouponType;
import com.bulkpurchase.domain.service.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ApplyCouponService {

    private final PercentageCouponService percentageCouponService;
    private final AmountCouponService amountCouponService;
    private final CouponService couponService;

    public Double applyCoupon(User user, Long couponID, Long productID, Double totalPrice) {
        Coupon coupon = couponService.findById(couponID).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "존재하지 않는 쿠폰입니다."));
        CouponType type = coupon.getType();
        if (type.equals(CouponType.AMOUNT_DISCOUNT)) {
            totalPrice = amountCouponService.calculateCouponSalePrice(user, coupon, productID, totalPrice);
        }
        if (type.equals(CouponType.PERCENTAGE_DISCOUNT)) {
            totalPrice = percentageCouponService.calculateCouponSalePrice(user, coupon, productID, totalPrice);
        }

        return totalPrice;
    }
}
