package com.bulkpurchase.domain.entity.coupon;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.CouponType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponID;

    @Column(nullable = false, unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponType type;

    @Column(name = "discount_amount", nullable = false)
    private Double discount;

    @Column(name = "valid_from", nullable = false)
    private LocalDateTime validFrom;

    @Column(name = "valid_until", nullable = false)
    private LocalDateTime validUntil;

    @Column(name = "minimum_order_amount", nullable = false)
    private Double minimumOrderAmount;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CouponApplicableProduct> applicableProducts = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdBy;

}