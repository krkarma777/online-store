package com.bulkpurchase.domain.dto.order;

import com.bulkpurchase.domain.entity.order.OrderDetail;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailResponseDTO {
    private String productName;
    private Integer quantity;
    private Double price;
    private String shippingCompany;
    private String shippingNumber;
    private String orderDetailStatus;

    public OrderDetailResponseDTO(OrderDetail orderDetail) {
        this.productName = orderDetail.getProduct().getProductName();
        this.quantity = orderDetail.getQuantity();
        this.price = orderDetail.getPrice();
        this.shippingCompany = orderDetail.getShippingCompany();
        this.shippingNumber = orderDetail.getShippingNumber();
        this.orderDetailStatus = orderDetail.getStatus().getDescription();
    }
}
