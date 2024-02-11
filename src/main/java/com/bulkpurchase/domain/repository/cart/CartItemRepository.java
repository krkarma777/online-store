package com.bulkpurchase.domain.repository.cart;

import com.bulkpurchase.domain.entity.cart.Cart;
import com.bulkpurchase.domain.entity.cart.CartItem;
import com.bulkpurchase.domain.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartAndProduct(Cart cart, Product product);

}
