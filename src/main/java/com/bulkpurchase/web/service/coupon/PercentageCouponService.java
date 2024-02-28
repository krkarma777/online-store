package com.bulkpurchase.web.service.coupon;

import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.validator.coupon.CouponValidatorImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PercentageCouponService implements CouponDiscountService{

    private final CouponValidatorImpl couponValidator;

    @Override
    public Double calculateCouponSalePrice(User user, Coupon coupon, Long productID, Double totalPrice) {
        boolean itValidCoupon = couponValidator.isItValidCoupon(user, coupon, productID, totalPrice);
        if (!itValidCoupon) {
            return 0.0;
        }
        return totalPrice * (1 - coupon.getDiscount() / 100);
    }
}
