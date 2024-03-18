package com.bulkpurchase.domain.service.cart;

import com.bulkpurchase.domain.entity.cart.Cart;
import com.bulkpurchase.domain.entity.cart.CartItem;
import com.bulkpurchase.domain.entity.product.Product;
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
    }

    public void deleteCartItemsByIds(List<Long> itemIds) {
        cartItemRepository.deleteAllById(itemIds);
    }

    public Optional<CartItem> findById(Long itemId) {
        return cartItemRepository.findById(itemId);
    }

    public List<CartItem> findByCartAndProduct(Cart cart, Product product) {
        return cartItemRepository.findByCartAndProduct(cart, product);
    }

    public CartItem addProductToCart(Cart cart, Product product, int quantity) {
        return cartItemRepository.save(new CartItem(cart, product, quantity));
    }

    public void delete(CartItem item) {
        cartItemRepository.delete(item);
    }

    public void deleteAll(List<CartItem> itemsToDelete) {
        cartItemRepository.deleteAll(itemsToDelete);
    }
}
