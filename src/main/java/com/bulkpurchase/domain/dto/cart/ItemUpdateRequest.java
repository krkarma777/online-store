package com.bulkpurchase.domain.dto.cart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemUpdateRequest {
    private Long itemId;
    private Integer quantity;
}
