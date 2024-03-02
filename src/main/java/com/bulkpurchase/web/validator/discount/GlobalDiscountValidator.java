package com.bulkpurchase.web.validator.discount;

import com.bulkpurchase.domain.dto.discount.GlobalDiscountModel;
import com.bulkpurchase.domain.enums.DiscountType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class GlobalDiscountValidator {

    public boolean isGlobalDiscountValid(GlobalDiscountModel globalDiscountModel) {
        LocalDateTime validFrom = globalDiscountModel.getValidFrom();
        LocalDateTime validUntil = globalDiscountModel.getValidUntil();
        LocalDateTime now = LocalDateTime.now();

        DiscountType discountType = globalDiscountModel.getDiscountType();
        Boolean isActive = globalDiscountModel.getIsActive();
        Double discount = globalDiscountModel.getDiscount();

        return validFrom.isBefore(now) && validUntil.isAfter(now)
                && discountType != null
                && isActive
                && discount != 0.0;
    }
}
