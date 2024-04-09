package com.bulkpurchase.domain.dto.orderdetail;

import com.bulkpurchase.domain.entity.order.OrderDetail;
import lombok.Getter;
import lombok.Setter;

import static com.bulkpurchase.CustomTimeFormatter.timeFormat;

@Getter
@Setter
public class OrderDetailManagementResponseDTO {

    private Long productID;

    private String productName;

    private Long orderDetailID;

    private Integer quantity;

    private Double price;

    private String orderDetailStatus;

    private String shippingCompany;

    private String shippingNumber;

    private String orderDate;

    public OrderDetailManagementResponseDTO(OrderDetail orderDetail) {
        this.productID = orderDetail.getProduct().getProductID();
        this.productName = orderDetail.getProduct().getProductName();
        this.orderDetailID = orderDetail.getOrderDetailID();
        this.quantity = orderDetail.getQuantity();
        this.price = orderDetail.getPrice();
        this.orderDetailStatus = orderDetail.getStatus().getDescription();
        this.shippingCompany = orderDetail.getShippingCompany();
        this.shippingNumber = orderDetail.getShippingNumber();
        this.orderDate = timeFormat(orderDetail.getOrder().getOrderDate());
    }

    public OrderDetailManagementResponseDTO() {
    }
}
