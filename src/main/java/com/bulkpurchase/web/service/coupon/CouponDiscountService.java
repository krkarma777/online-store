package com.bulkpurchase.web.service.coupon;

import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.user.User;

public interface CouponDiscountService {
    Double calculateCouponSalePrice(User user, Coupon coupon, Long productID, Double totalPrice);
}
