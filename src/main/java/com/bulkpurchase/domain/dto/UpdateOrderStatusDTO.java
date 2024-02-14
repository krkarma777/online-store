package com.bulkpurchase.domain.dto;

import com.bulkpurchase.domain.enums.OrderStatus;

public class UpdateOrderStatusDTO {

    private Long orderDetailID;
    private OrderStatus status;

    // 기본 생성자
    public UpdateOrderStatusDTO() {
    }

    // 모든 필드를 포함한 생성자
    public UpdateOrderStatusDTO(Long orderDetailID, OrderStatus status) {
        this.orderDetailID = orderDetailID;
        this.status = status;
    }

    // getter 및 setter
    public Long getOrderDetailID() {
        return orderDetailID;
    }

    public void setOrderDetailID(Long orderDetailID) {
        this.orderDetailID = orderDetailID;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    // toString 메소드는 로깅이나 디버깅 목적으로 유용합니다.
    @Override
    public String toString() {
        return "UpdateOrderStatusRequest{" +
                "orderDetailID=" + orderDetailID +
                ", status=" + status +
                '}';
    }
}
