package com.bulkpurchase.domain.repository.order;

import com.bulkpurchase.domain.dto.orderdetail.OrderDetailNameAndIdDTO;
import com.bulkpurchase.domain.entity.order.Order;
import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrder(Order order);

    List<OrderDetail> findByProductOrderByOrderDetailIDDesc(Product product);

    @Query("SELECT new com.bulkpurchase.domain.dto.orderdetail.OrderDetailNameAndIdDTO(od.order.orderID, p.productName) " +
            "FROM OrderDetail od JOIN od.product p WHERE p.user = :user " +
            "ORDER BY od.orderDetailID DESC")
    List<OrderDetailNameAndIdDTO> findOrderIDAndProductNameByUser(@Param("user") User user, Pageable pageable);

    Page<OrderDetail> findByProductUser(User user, Pageable pageable);
}
