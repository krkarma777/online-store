package com.bulkpurchase.domain.service.order;

import com.bulkpurchase.domain.entity.Order;
import com.bulkpurchase.domain.entity.User;
import com.bulkpurchase.domain.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public Order saveOrder(User user, Double totalPrice) {
        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(totalPrice);
        System.out.println("order = " + order);
        return orderRepository.save(order);
    }
}