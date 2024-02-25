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

    public static UserRole fromDescription(String description) {
        for (UserRole role : UserRole.values()) {
            if (role.getDescription().equals(description)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + description + "]");
    }

    public static UserRole fromRoleString(String roleStr) {
        String description = roleStr.replace("ROLE_", ""); // 접두사 제거
        return fromDescription(description);
    }

}
