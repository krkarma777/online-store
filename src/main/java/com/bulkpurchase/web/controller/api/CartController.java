package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.cart.ItemAddRequest;
import com.bulkpurchase.domain.dto.cart.ItemDeleteRequest;
import com.bulkpurchase.domain.dto.cart.ItemUpdateRequest;
import com.bulkpurchase.domain.entity.cart.Cart;
import com.bulkpurchase.domain.entity.cart.CartItem;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.cart.CartItemService;
import com.bulkpurchase.domain.service.cart.CartService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/cart/item")
@RequiredArgsConstructor
public class CartController {

    private final CartItemService cartItemService;
    private final CartService cartService;
    private final ProductService productService;
    private final UserAuthValidator userAuthValidator;

    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody ItemAddRequest itemAddRequest,
                                       Principal principal) {
        Long productID = itemAddRequest.getProductID();
        Integer quantity = itemAddRequest.getQuantity();

        Optional<Product> productOpt = productService.findById(productID);
        if (productOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "해당 상품이 존재하지 않습니다."));
        }

        User user = userAuthValidator.getCurrentUser(principal);
        Cart cart = cartService.cartFindOrCreate(user);

        List<CartItem> existingItems = cartItemService.findByCartAndProduct(cart, productOpt.get());

        if (!existingItems.isEmpty()) {
            CartItem existingItem = existingItems.get(0);
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemService.saveCartItem(existingItem);
        } else {
            cartItemService.saveCartItem(new CartItem(cart, productOpt.get(), quantity));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "상품이 장바구니에 추가되었습니다."));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCartItem(@RequestBody ItemDeleteRequest itemDeleteRequest,
                                            Principal principal) {
        System.out.println("itemDeleteRequest = " + itemDeleteRequest);
        List<Long> itemIds = itemDeleteRequest.getItemIds();
        Long itemId = itemDeleteRequest.getItemId();
        if (itemIds.isEmpty() && itemId != null) {
            User user = userAuthValidator.getCurrentUser(principal);

            Optional<CartItem> cartItemOpt = cartItemService.findById(itemId);
            if (cartItemOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "상품을 찾을 수 없습니다."));
            }

            CartItem cartItem = cartItemOpt.get();
            if (!cartItem.getCart().getUser().equals(user)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "이 상품을 삭제할 권한이 없습니다."));
            }

            cartItemService.deleteCartItemById(itemId);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "상품이 삭제되었습니다."));
        } else if (itemId == null && !itemIds.isEmpty()) {
            cartItemService.deleteCartItemsByIds(itemIds);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "상품이 삭제되었습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "상품을 선택해주십시오."));
        }
    }

    @PatchMapping
    public ResponseEntity<?> updateItem(@RequestBody ItemUpdateRequest itemUpdateRequest) {
        Optional<CartItem> byId = cartItemService.findById(itemUpdateRequest.getItemId());

        if (byId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "해당 아이템이 존재하지 않습니다."));
        } else {
            CartItem cartItem = byId.get();
            cartItem.setQuantity(itemUpdateRequest.getQuantity());
            cartItemService.saveCartItem(cartItem);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "상품이 수정되었습니다."));
        }
    }
}
