package com.bulkpurchase.domain.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirectPurchaseResponseDTO {
    private Long cartID;
    private Long itemID;

    public DirectPurchaseResponseDTO(Long cartID, Long itemID) {
        this.cartID = cartID;
        this.itemID = itemID;
    }
}
