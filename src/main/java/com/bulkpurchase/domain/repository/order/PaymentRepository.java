package com.bulkpurchase.domain.repository.order;

import com.bulkpurchase.domain.entity.order.Order;
import com.bulkpurchase.domain.entity.order.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByOrder(Order order);
}
