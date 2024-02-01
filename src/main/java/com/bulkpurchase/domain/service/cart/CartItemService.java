package com.bulkpurchase.domain.service.cart;

import com.bulkpurchase.domain.entity.CartItem;
import com.bulkpurchase.domain.repository.cart.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    public void saveCartItem(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }

    public void deleteCartItemById(Long itemId) {
        cartItemRepository.deleteById(itemId);
        System.out.println("실행됨");
    }

    public void deleteCartItemsByIds(List<Long> itemIds) {
        cartItemRepository.deleteAllById(itemIds);
    }

    public Optional<CartItem> findById(Long itemId) {
        return cartItemRepository.findById(itemId);
    }
}
