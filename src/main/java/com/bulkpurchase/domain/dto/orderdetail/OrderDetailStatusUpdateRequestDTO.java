package com.bulkpurchase.domain.dto.orderdetail;

import com.bulkpurchase.domain.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailStatusUpdateRequestDTO {

    private Long orderDetailID;
    private OrderStatus orderStatus;
}
