package com.bulkpurchase.domain.dto.coupon;

import com.bulkpurchase.domain.entity.coupon.Coupon;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class CouponResponseDTO {

    private Long couponID;
    private String code;
    private String type;
    private Double discount;
    private String validFrom;
    private String validUntil;
    private Double minimumOrderAmount;
    private Integer quantity;
    private String name;
    private String description;
    private Double maxDiscountAmount;

    public CouponResponseDTO(Coupon coupon) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        this.couponID = coupon.getCouponID();
        this.code = coupon.getCode();
        this.type = coupon.getType().getDescription();
        this.discount = coupon.getDiscount();
        this.validFrom = coupon.getValidFrom().format(formatter);
        this.validUntil = coupon.getValidUntil().format(formatter);
        this.minimumOrderAmount = coupon.getMinimumOrderAmount();
        this.quantity = coupon.getQuantity();
        this.name = coupon.getName();
        this.description = coupon.getDescription();
        this.maxDiscountAmount = coupon.getMaxDiscountAmount();
    }
}
