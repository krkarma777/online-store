package com.bulkpurchase.domain.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDTO {
    private Long productID;
    private Integer quantity;
}
