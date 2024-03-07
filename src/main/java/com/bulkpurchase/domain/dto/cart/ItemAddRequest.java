package com.bulkpurchase.domain.dto.cart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemAddRequest {
    private Long productID;
    private Integer quantity;
}
