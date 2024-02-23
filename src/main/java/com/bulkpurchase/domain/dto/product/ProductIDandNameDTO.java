package com.bulkpurchase.domain.dto.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductIDandNameDTO {

    private Long productID;
    private String productName;

    public ProductIDandNameDTO(Long productID, String productName) {
        this.productID = productID;
        this.productName = productName;
    }
}
