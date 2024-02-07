package com.bulkpurchase.domain.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("처리 대기 중"),
    PROCESSING("처리 중"),
    SHIPPED("배송 중"),
    DELIVERED("배송 완료"),
    CANCELLED("취소됨");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

}
