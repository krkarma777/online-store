package com.bulkpurchase.domain.service.order;

import com.bulkpurchase.domain.entity.Order;
import com.bulkpurchase.domain.entity.OrderDetail;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.repository.order.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;


    public OrderDetail save(Order order, Product product, Integer quantity) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setQuantity(quantity);
        orderDetail.setProduct(product);
        return orderDetailRepository.save(orderDetail);
    }
}
