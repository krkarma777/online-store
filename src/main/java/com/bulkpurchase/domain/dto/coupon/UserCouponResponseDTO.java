package com.bulkpurchase.domain.dto.coupon;

import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.coupon.UserCoupon;
import com.bulkpurchase.domain.enums.CouponType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserCouponResponseDTO {

    private Long UserCouponID;
    private Long couponID;
    private String code;
    private CouponType type;
    private Double discount;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private Double minimumOrderAmount;
    private Integer quantity;
    private String name;
    private String description;
    private Double maxDiscountAmount;
    private boolean used;

    public UserCouponResponseDTO(UserCoupon userCoupon) {
        this.UserCouponID = userCoupon.getUserCouponID();
        Coupon coupon = userCoupon.getCoupon();
        this.couponID = coupon.getCouponID();
        this.code = coupon.getCode();
        this.type = coupon.getType();
        this.discount = coupon.getDiscount();
        this.validFrom = coupon.getValidFrom();
        this.validUntil = coupon.getValidUntil();
        this.minimumOrderAmount = coupon.getMinimumOrderAmount();
        this.quantity = coupon.getQuantity();
        this.name = coupon.getName();
        this.description = coupon.getDescription();
        this.maxDiscountAmount = coupon.getMaxDiscountAmount();
        this.used = userCoupon.isUsed();
    }
}
