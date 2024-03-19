package com.bulkpurchase.domain.dto.order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDTO {

    private List<OrderItemDTO> orderItemDTOS;
    private Double totalPrice;
    private String paymentMethod;
}
