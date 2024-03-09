package com.bulkpurchase.domain.enums.inquiry;

import lombok.Getter;

@Getter
public enum InquiryType {

    COMPLAINT("불만 사항"),
    GENERAL("일반 문의"),
    TECH_SUPPORT("기술 지원");


    private final String description;

    InquiryType(String description) {
        this.description = description;
    }
}
