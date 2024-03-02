package com.bulkpurchase.domain.dto.coupon;

import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.enums.CouponType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CouponOrderSearchDTO {

    private Long couponID;
    private String name;
    private String description;
    private CouponType type;
    private Double discount;
    private Double minimumOrderAmount;
    private Double maxDiscountAmount;
    private LocalDateTime validUntil;

    public CouponOrderSearchDTO(Coupon coupon) {
        this.couponID = coupon.getCouponID();
        this.name = coupon.getName();
        this.description = coupon.getDescription();
        this.type = coupon.getType();
        this.discount = coupon.getDiscount();
        this.minimumOrderAmount = coupon.getMinimumOrderAmount();
        this.maxDiscountAmount = coupon.getMaxDiscountAmount();
        this.validUntil = coupon.getValidUntil();
    }
}
