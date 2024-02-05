package com.bulkpurchase.domain.repository.order;

import com.bulkpurchase.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
