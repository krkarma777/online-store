package com.bulkpurchase.domain.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDTO {
    private Long orderId;
    private Long userId;
    private Date orderDate;
    private Double totalPrice;
}
