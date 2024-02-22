package com.bulkpurchase.web.validator.coupon;

import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.user.User;

public interface CouponValidator {

    boolean isItValidCoupon(User user, Coupon coupon, Long productID, Double totalPrice);
}
