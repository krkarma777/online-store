package com.bulkpurchase.domain.service.cart;

import com.bulkpurchase.domain.entity.CartItem;
import com.bulkpurchase.domain.repository.cart.CartItemRepository;
import com.bulkpurchase.domain.repository.cart.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    public void saveCartItem(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }
}
