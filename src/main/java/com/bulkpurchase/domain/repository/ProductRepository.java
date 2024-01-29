package com.bulkpurchase.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bulkpurchase.domain.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
