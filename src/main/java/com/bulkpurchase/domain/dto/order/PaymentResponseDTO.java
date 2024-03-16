package com.bulkpurchase.domain.dto.order;

import com.bulkpurchase.domain.entity.order.Payment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseDTO {
    private String paymentDate;
    private Double paymentAmount;
    private String paymentMethod;
    private String paymentStatus;

    public PaymentResponseDTO(Payment payment) {
        this.paymentDate = payment.getPaymentDate().toString();
        this.paymentAmount = payment.getAmount();
        this.paymentMethod = payment.getPaymentMethod();
        this.paymentStatus = payment.getStatus();
    }
}
