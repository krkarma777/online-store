package com.bulkpurchase.web.policy.discount;

import com.bulkpurchase.domain.entity.discount.GlobalDiscount;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class GlobalAmountDiscountPolicy implements GlobalDiscountPolicy {

    public Double discount(GlobalDiscount globalDiscount, Double totalPrice) {
        double applyPrice = Math.max(0, totalPrice - globalDiscount.getDiscount());
        Double maxDiscountAmount = globalDiscount.getMaxDiscountAmount();
        if (applyPrice > maxDiscountAmount) {
            return applyPrice;
        } else {
            return totalPrice - maxDiscountAmount;
        }
    }
}
