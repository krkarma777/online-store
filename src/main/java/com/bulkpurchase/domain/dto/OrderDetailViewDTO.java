package com.bulkpurchase.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailViewDTO {
    private Long orderDetailID;
    private ProductViewDTO product;
    private Integer quantity;
    private Double price;

    public OrderDetailViewDTO(Long orderDetailID, ProductViewDTO product, Integer quantity, Double price) {
        this.orderDetailID = orderDetailID;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }


    public OrderDetailViewDTO() {
    }
}
