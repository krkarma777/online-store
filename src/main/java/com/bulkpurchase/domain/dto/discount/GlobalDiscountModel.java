package com.bulkpurchase.domain.dto.discount;

import com.bulkpurchase.domain.enums.DiscountType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GlobalDiscountModel {

    private String name;
    private Double discount;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private DiscountType discountType;
    private Double minimumOrderAmount;
    private Double maxDiscountAmount;
    private String description;
    private Boolean isActive;
}
