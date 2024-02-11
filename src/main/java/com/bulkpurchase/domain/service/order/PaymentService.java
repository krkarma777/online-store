package com.bulkpurchase.domain.service.order;

import com.bulkpurchase.domain.entity.order.Payment;
import com.bulkpurchase.domain.repository.order.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }
}
