package com.bulkpurchase.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CouponRequestDTO {

    private Long productID;
    private Double totalPrice;
}
