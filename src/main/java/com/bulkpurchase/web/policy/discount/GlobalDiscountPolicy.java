package com.bulkpurchase.web.policy.discount;

import com.bulkpurchase.domain.entity.discount.GlobalDiscount;

public interface GlobalDiscountPolicy {

    Double discount(GlobalDiscount globalDiscount, Double totalPrice);
}
