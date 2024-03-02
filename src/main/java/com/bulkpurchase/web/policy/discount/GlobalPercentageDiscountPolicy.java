package com.bulkpurchase.web.policy.discount;

import com.bulkpurchase.domain.dto.discount.GlobalDiscountModel;
import org.springframework.stereotype.Service;

@Service
public class GlobalPercentageDiscountPolicy implements GlobalDiscountPolicy {
    public Double discount(GlobalDiscountModel globalDiscountModel, Double totalPrice) {
        return totalPrice * (1 - globalDiscountModel.getDiscount() / 100);
    }
}
