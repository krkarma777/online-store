package com.bulkpurchase.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bulkpurchase.domain.entity.product.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
