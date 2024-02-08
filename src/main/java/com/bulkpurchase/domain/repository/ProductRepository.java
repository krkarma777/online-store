package com.bulkpurchase.domain.repository;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.entity.product.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUserOrderByProductIDDesc(User user);

    @Query("SELECT od.product as product, COUNT(od) as orderCount FROM OrderDetail od GROUP BY od.product ORDER BY orderCount DESC")
    List<Object[]> findPopularProducts(Pageable pageable);


}
