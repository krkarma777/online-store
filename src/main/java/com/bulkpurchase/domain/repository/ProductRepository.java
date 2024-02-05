package com.bulkpurchase.domain.repository;

import com.bulkpurchase.domain.entity.User;
import com.bulkpurchase.domain.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUserOrderByProductIDDesc(User user);

}
