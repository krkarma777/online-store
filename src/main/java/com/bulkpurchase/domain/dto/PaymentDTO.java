package com.bulkpurchase.domain.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PaymentDTO {
    private Long paymentId;
    private Long orderId;
    private Date paymentDate;
    private Double amount;
    private String paymentMethod;
}
