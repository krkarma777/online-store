package com.bulkpurchase.domain.dto;

import com.bulkpurchase.domain.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderViewDTO {
    private Long orderID;
    private Date orderDate;
    private Double totalPrice;
    private OrderStatus status;
    private List<OrderDetailViewDTO> orderDetails;

    public OrderViewDTO(Long orderID, Date orderDate, Double totalPrice, OrderStatus status, List<OrderDetailViewDTO> orderDetails) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderDetails = orderDetails;
    }

    public OrderViewDTO() {
    }
}
