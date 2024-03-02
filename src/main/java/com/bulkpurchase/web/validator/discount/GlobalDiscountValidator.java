package com.bulkpurchase.web.validator.discount;

import com.bulkpurchase.domain.dto.discount.GlobalDiscountModel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class GlobalDiscountValidator {

    public boolean isGlobalDiscountValid(GlobalDiscountModel globalDiscountModel) {
        LocalDateTime validFrom = globalDiscountModel.getValidFrom();
        LocalDateTime validUntil = globalDiscountModel.getValidUntil();
        LocalDateTime now = LocalDateTime.now();
        return validFrom.isBefore(now) && validUntil.isAfter(now);
    }
}
