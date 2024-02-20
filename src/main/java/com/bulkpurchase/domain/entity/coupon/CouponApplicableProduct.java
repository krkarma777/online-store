package com.bulkpurchase.domain.entity.coupon;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CouponID", nullable = false)
    private Coupon coupon;

    @Column(name = "ProductID", nullable = false)
    private Long productId;
}