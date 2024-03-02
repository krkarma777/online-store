package com.bulkpurchase.web.validator.discount;

import com.bulkpurchase.domain.entity.discount.GlobalDiscount;
import com.bulkpurchase.domain.enums.DiscountType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class GlobalDiscountValidator {

    public boolean isGlobalDiscountValid(GlobalDiscount globalDiscount, Double orderAmount) {
        LocalDateTime validFrom = globalDiscount.getValidFrom();
        LocalDateTime validUntil = globalDiscount.getValidUntil();
        LocalDateTime now = LocalDateTime.now();

        DiscountType discountType = globalDiscount.getDiscountType();
        Boolean isActive = globalDiscount.getIsActive();
        Double discount = globalDiscount.getDiscount();
        Double minimumOrderAmount = globalDiscount.getMinimumOrderAmount();

        return validFrom.isBefore(now) && validUntil.isAfter(now)
                && discountType != null
                && isActive
                && discount != 0.0
                && orderAmount >= minimumOrderAmount;
    }
}
