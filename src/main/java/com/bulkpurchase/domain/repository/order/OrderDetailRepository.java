package com.bulkpurchase.domain.repository.order;

import com.bulkpurchase.domain.entity.Order;
import com.bulkpurchase.domain.entity.OrderDetail;
import com.bulkpurchase.domain.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrder(Order order);

    List<OrderDetail> findByProductOrderByOrderDetailIDDesc(Product product);

}
