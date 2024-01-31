package com.bulkpurchase.domain.repository.order;

import com.bulkpurchase.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
