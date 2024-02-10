package com.bulkpurchase.domain.enums;

public enum UserRole {
    ROLE_자영업자("자영업자"),
    ROLE_판매자("판매자"),
    ROLE_관리자("관리자");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
