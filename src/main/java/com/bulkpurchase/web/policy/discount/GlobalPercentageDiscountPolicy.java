package com.bulkpurchase.web.policy.discount;

import com.bulkpurchase.domain.entity.discount.GlobalDiscount;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class GlobalPercentageDiscountPolicy implements GlobalDiscountPolicy {
    public Double discount(GlobalDiscount globalDiscount, Double totalPrice) {
        double applyPrice = totalPrice * (1 - globalDiscount.getDiscount() / 100);
        Double maxDiscountAmount = globalDiscount.getMaxDiscountAmount();
        if (applyPrice > maxDiscountAmount) {
            return applyPrice;
        } else {
            return totalPrice - maxDiscountAmount;
        }
    }
}
