package com.bulkpurchase.domain.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum DiscountType {
    PERCENTAGE_DISCOUNT("비율 할인"),
    AMOUNT_DISCOUNT("금액 할인");

    private final String description;

    DiscountType(String description) {
        this.description = description;
    }
}
