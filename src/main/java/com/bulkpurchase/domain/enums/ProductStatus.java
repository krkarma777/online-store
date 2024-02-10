package com.bulkpurchase.domain.enums;

public enum ProductStatus {

    ACTIVE("활성"), // 활성
    INACTIVE("비활성"), // 비활성
    DISCONTINUED("판매 중지"); // 판매 중지

    private final String description;

    ProductStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
