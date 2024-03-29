package com.bulkpurchase.domain.entity.coupon;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "coupon_applicable_product")
public class CouponApplicableProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CouponApplicableProductID;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CouponID", nullable = false)
    private Coupon coupon;

    @Column(name = "ProductID", nullable = false)
    private Long productId;


    public CouponApplicableProduct(Coupon coupon, Long productId) {
        this.coupon = coupon;
        this.productId = productId;
    }

    public CouponApplicableProduct() {
    }
}