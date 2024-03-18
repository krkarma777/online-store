package com.bulkpurchase.web.service;

import com.bulkpurchase.domain.dto.cart.CartItemOrderResponseDTO;
import com.bulkpurchase.domain.dto.order.DirectPurchaseRequestDTO;
import com.bulkpurchase.domain.dto.order.DirectPurchaseResponseDTO;
import com.bulkpurchase.domain.entity.cart.Cart;
import com.bulkpurchase.domain.entity.cart.CartItem;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.cart.CartItemService;
import com.bulkpurchase.domain.service.cart.CartService;
import com.bulkpurchase.domain.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final ProductService productService;
    private final CartService cartService;

    public CartItemOrderResponseDTO processDirectPurchase(Long productID, Integer quantity) {
        Product product = productService.findById(productID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다."));
        return new CartItemOrderResponseDTO(productID, product.getProductName(), product.getPrice(), quantity);
    }

    public List<CartItemOrderResponseDTO> processCartPurchase(Long cartID, List<Long> itemIDs, User user) {
        Cart cart = cartService.findById(cartID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "장바구니가 존재하지 않습니다."));

        if (!cart.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 요청입니다.");
        }

        return cart.getItems().stream()
                .filter(cartItem -> itemIDs.contains(cartItem.getCartItemID()))
                .map(CartItemOrderResponseDTO::new)
                .toList();
    }
}
