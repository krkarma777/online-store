package com.bulkpurchase.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailViewModel {
    private Long orderDetailID;
    private ProductViewModel product;
    private Integer quantity;
    private Double price;

    public OrderDetailViewModel(Long orderDetailID, ProductViewModel product, Integer quantity, Double price) {
        this.orderDetailID = orderDetailID;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }


    public OrderDetailViewModel() {
    }
}
