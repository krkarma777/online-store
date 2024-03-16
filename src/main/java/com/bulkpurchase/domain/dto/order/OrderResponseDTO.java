package com.bulkpurchase.domain.dto.order;

import com.bulkpurchase.domain.entity.order.Order;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponseDTO {

    private Long orderID;
    private String orderDate;
    private Double totalPrice;
    private String orderStatus;

    public OrderResponseDTO(Order order) {
        this.orderID = order.getOrderID();
        this.orderDate = order.getOrderDate().toString();
        this.totalPrice = order.getTotalPrice();
        this.orderStatus = order.getStatus().getDescription();
    }
}
