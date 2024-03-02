package com.bulkpurchase.domain.entity.discount;

import com.bulkpurchase.domain.enums.DiscountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "global_discounts") // 데이터베이스에서 사용할 테이블 이름 지정
@Getter
@Setter
@ToString
public class GlobalDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long globalDiscountID; // Unique identifier

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double discount;

    @Column(name = "valid_from", nullable = false)
    private LocalDateTime validFrom;

    @Column(name = "valid_until", nullable = false)
    private LocalDateTime validUntil;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    private DiscountType discountType;

    @Column(name = "minimum_order_amount")
    private Double minimumOrderAmount;

    @Column(name = "max_discount_amount")
    private Double maxDiscountAmount;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Boolean isActive;
}
