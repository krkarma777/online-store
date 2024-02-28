package com.bulkpurchase.domain.validator.coupon;

import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.coupon.UserCoupon;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.coupon.CouponApplicableProductService;
import com.bulkpurchase.domain.service.coupon.UserCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CouponValidatorImpl implements CouponValidator {

    private final CouponApplicableProductService couponApplicableProductService;
    private final UserCouponService userCouponService;

    @Override
    public boolean isItValidCoupon(User user, Coupon coupon, Long productID, Double totalPrice) {
        return isWithinValidPeriod(coupon) &&
                isApplicableToProduct(coupon, productID) &&
                isUserEligibleForCoupon(user, coupon) &&
                isOrderEligibleForCoupon(coupon, totalPrice);
    }

    private boolean isWithinValidPeriod(Coupon coupon) {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(coupon.getValidFrom()) && now.isBefore(coupon.getValidUntil());
    }

    private boolean isApplicableToProduct(Coupon coupon, Long productID) {
        return couponApplicableProductService.findByCouponAndProductId(coupon, productID).isPresent();
    }

    private boolean isUserEligibleForCoupon(User user, Coupon coupon) {
        Optional<UserCoupon> userCouponOpt = userCouponService.findByUserAndCoupon(user, coupon);
        return userCouponOpt.isPresent() && !userCouponOpt.get().isUsed();
    }

    private boolean isOrderEligibleForCoupon(Coupon coupon, Double totalPrice) {
        return totalPrice >= coupon.getMinimumOrderAmount();
    }
}
