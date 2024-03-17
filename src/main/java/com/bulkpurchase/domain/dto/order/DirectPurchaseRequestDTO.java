package com.bulkpurchase.domain.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirectPurchaseRequestDTO {

    private Long productID;
    private int quantity;
}
