package com.bulkpurchase.domain.repository.cart;

import com.bulkpurchase.domain.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
