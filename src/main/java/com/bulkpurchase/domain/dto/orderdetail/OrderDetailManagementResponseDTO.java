package com.bulkpurchase.domain.dto.orderdetail;

import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import static com.bulkpurchase.CustomTimeFormatter.timeFormat;

@Getter
@Setter
public class OrderDetailManagementResponseDTO {

    private Long productId;

    private String productName;

    private Long orderDetailId;

    private Integer quantity;

    private Double price;

    private OrderStatus orderDetailStatus;

    private String shippingCompany;

    private String shippingNumber;

    private String orderDate;

    public OrderDetailManagementResponseDTO(OrderDetail orderDetail) {
        this.productId = orderDetail.getProduct().getProductID();
        this.productName = orderDetail.getProduct().getProductName();
        this.orderDetailId = orderDetail.getOrderDetailID();
        this.quantity = orderDetail.getQuantity();
        this.price = orderDetail.getPrice();
        this.orderDetailStatus = orderDetail.getStatus();
        this.shippingCompany = orderDetail.getShippingCompany();
        this.shippingNumber = orderDetail.getShippingNumber();
        this.orderDate = timeFormat(orderDetail.getOrder().getOrderDate());
    }
}
