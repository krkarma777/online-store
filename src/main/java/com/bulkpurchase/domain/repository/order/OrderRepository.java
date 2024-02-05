package com.bulkpurchase.domain.repository.order;

import com.bulkpurchase.domain.entity.Order;
import com.bulkpurchase.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
