package com.bulkpurchase.domain.enums.inquiry;

import lombok.Getter;

@Getter
public enum InquiryStatus {

    PENDING("대기 중"),
    IN_PROGRESS("처리 중"),
    COMPLETED("완료");

    private final String description;

    InquiryStatus(String description) {
        this.description = description;
    }
}
