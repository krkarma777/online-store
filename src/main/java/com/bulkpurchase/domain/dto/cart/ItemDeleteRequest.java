package com.bulkpurchase.domain.dto.cart;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ItemDeleteRequest {
    private Long itemId;
    private List<Long> itemIds;
}
