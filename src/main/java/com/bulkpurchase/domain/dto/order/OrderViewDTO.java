package com.bulkpurchase.domain.dto.order;

import com.bulkpurchase.domain.dto.orderdetail.OrderDetailViewDTO;
import com.bulkpurchase.domain.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
public class OrderViewDTO {
    private Long orderID;
    private String orderDate;
    private Double totalPrice;
    private OrderStatus status;
    private List<OrderDetailViewDTO> orderDetails;

    public OrderViewDTO(Long orderID, LocalDateTime orderDate, Double totalPrice, OrderStatus status, List<OrderDetailViewDTO> orderDetails) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        this.orderID = orderID;
        this.orderDate = orderDate.format(formatter);
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderDetails = orderDetails;
    }

    public OrderViewDTO() {
    }
}
