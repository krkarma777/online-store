package com.bulkpurchase.domain.repository.cart;

import com.bulkpurchase.domain.entity.Cart;
import com.bulkpurchase.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);

}
