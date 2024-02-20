package com.bulkpurchase.domain.enums;

import lombok.Getter;

@Getter
public enum CouponType {
    PERCENTAGE_DISCOUNT("비율 할인"),
    AMOUNT_DISCOUNT("금액 할인"),
    FREE_SHIPPING("무료 배송");

    private final String description;

    CouponType(String description) {
        this.description = description;
    }
}
