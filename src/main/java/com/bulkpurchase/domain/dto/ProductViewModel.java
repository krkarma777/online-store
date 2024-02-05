package com.bulkpurchase.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductViewModel {
    private Long productID;
    private String name;
    private Double price;
    private String description;

    public ProductViewModel(Long productID, String name, Double price, String description) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public ProductViewModel() {
    }
}
