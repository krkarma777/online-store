package com.bulkpurchase.domain.dto;

import lombok.Data;

@Data
public class OrderDetailDTO {
    private Long orderDetailId;
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private Double price;
}
