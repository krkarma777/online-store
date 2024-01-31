package com.bulkpurchase.domain.repository;

import com.bulkpurchase.domain.entity.User;
import com.bulkpurchase.domain.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUserOrderByProductIDDesc(User user);

}
