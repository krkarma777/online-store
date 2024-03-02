package com.bulkpurchase.domain.repository;

import com.bulkpurchase.domain.entity.discount.GlobalDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalDiscountRepository extends JpaRepository<GlobalDiscount, Long> {

}
