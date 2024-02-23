package com.bulkpurchase.web.service;

import com.bulkpurchase.domain.entity.order.Order;
import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.enums.OrderStatus;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderStatusUpdateService {

    private final OrderDetailService orderDetailService;
    private final OrderService orderService;

    public void updateOrderStatus(OrderDetail orderDetail, OrderStatus status) {
        orderDetail.setStatus(status);
        orderDetailService.save(orderDetail);

        Order order = orderDetail.getOrder();
        List<OrderDetail> details = orderDetailService.findByOrder(order);

        boolean allDelivered = details.stream().allMatch(d -> d.getStatus() == OrderStatus.DELIVERED);
        boolean anyCancelled = details.stream().anyMatch(d -> d.getStatus() == OrderStatus.CANCELLED);
        boolean anyShipped = details.stream().anyMatch(d -> d.getStatus() == OrderStatus.SHIPPED);
        boolean allProcessingOrShipped = details.stream().allMatch(d ->
                d.getStatus() == OrderStatus.PROCESSING || d.getStatus() == OrderStatus.SHIPPED);

        if (anyCancelled) {
            order.setStatus(OrderStatus.CANCELLED);
        } else if (allDelivered) {
            order.setStatus(OrderStatus.DELIVERED);
        } else if (anyShipped && allProcessingOrShipped) {
            order.setStatus(OrderStatus.SHIPPED);
        } else {
            order.setStatus(OrderStatus.PROCESSING);
        }

        orderService.save(order);
    }
}
