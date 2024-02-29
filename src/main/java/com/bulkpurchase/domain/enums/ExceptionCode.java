package com.bulkpurchase.domain.enums;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    PAY_CANCEL("결제 취소"),
    PAY_FAILED("결제 실패");

    private final String description;

    ExceptionCode(String description) {
        this.description = description;
    }
}
