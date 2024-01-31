package com.bulkpurchase.domain.repository;

import com.bulkpurchase.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bulkpurchase.domain.entity.product.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUserOrderByProductIDDesc(User user);

}
