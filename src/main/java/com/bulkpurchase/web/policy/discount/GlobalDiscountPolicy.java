package com.bulkpurchase.web.policy.discount;

import com.bulkpurchase.domain.dto.discount.GlobalDiscountModel;

public interface GlobalDiscountPolicy {

    Double discount(GlobalDiscountModel globalDiscountModel, Double totalPrice);
}
