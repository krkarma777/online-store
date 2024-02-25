package com.bulkpurchase.domain.service.cart;

import com.bulkpurchase.domain.entity.cart.Cart;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.repository.cart.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;

    public Cart cartFindOrCreate(User user) {
        System.out.println("user = " + user);
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
    public Optional<Cart> findById(Long cartID) {
        return cartRepository.findById(cartID);
    }

    public void deleteById(Long id) {
        cartRepository.deleteById(id);
    }

    public void save(Cart cart) {
        cartRepository.save(cart);
    }
}