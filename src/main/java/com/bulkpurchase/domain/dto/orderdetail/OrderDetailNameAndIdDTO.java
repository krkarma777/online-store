package com.bulkpurchase.domain.dto.orderdetail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailNameAndIdDTO {

    private Long orderID;
    private String productName;

    public OrderDetailNameAndIdDTO(Long orderID, String productName) {
        this.orderID = orderID;
        this.productName = productName;
    }

    public OrderDetailNameAndIdDTO() {
    }
}
