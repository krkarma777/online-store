package com.bulkpurchase.web.policy.discount;

import com.bulkpurchase.domain.dto.discount.GlobalDiscountModel;
import org.springframework.stereotype.Service;

@Service
public class GlobalAmountDiscountPolicy implements GlobalDiscountPolicy {

    public Double discount(GlobalDiscountModel globalDiscountModel, Double totalPrice) {
        return Math.max(0, totalPrice - globalDiscountModel.getDiscount());
    }
}
