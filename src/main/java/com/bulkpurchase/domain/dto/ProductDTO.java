package com.bulkpurchase.domain.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private Long productId;
    private String productName;
    private String description;
    private Double price;
    private Integer stock;
    private String imageUrl;
}
