package com.bulkpurchase.domain.dto.coupon;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CouponApplicableProductRequestDTO {

    @NotEmpty
    private Long couponID;

    private List<Long> productIDs;
}
