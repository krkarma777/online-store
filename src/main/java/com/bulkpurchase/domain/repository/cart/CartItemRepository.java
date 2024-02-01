package com.bulkpurchase.domain.repository.cart;

import com.bulkpurchase.domain.entity.Cart;
import com.bulkpurchase.domain.entity.CartItem;
import com.bulkpurchase.domain.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartAndProduct(Cart cart, Product product);

}
