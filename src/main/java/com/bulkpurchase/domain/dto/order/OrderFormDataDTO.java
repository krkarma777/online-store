package com.bulkpurchase.domain.dto.order;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class OrderFormDataDTO {

    private Map<Long, Integer> productQuantities;
    private double totalPrice;
    private String paymentMethod;

    public OrderFormDataDTO(Map<Long, Integer> productQuantities, double totalPrice, String paymentMethod) {
        this.productQuantities = productQuantities;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
    }

    public OrderFormDataDTO() {
    }
}
