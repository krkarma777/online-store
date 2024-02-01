package com.bulkpurchase.domain.service.cart;

import com.bulkpurchase.domain.entity.Cart;
import com.bulkpurchase.domain.entity.User;
import com.bulkpurchase.domain.repository.cart.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public Cart cartFindOrCreate(User user) {
        Optional<Cart> existingCart = cartRepository.findByUser(user);
        if (existingCart.isPresent()) {
            return existingCart.get();
        } else {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart); // 새 장바구니 저장
        }
    }

    public Optional<Cart> findByUser(User user) {
        return cartRepository.findByUser(user);
    }
}