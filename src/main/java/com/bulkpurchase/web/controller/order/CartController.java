package com.bulkpurchase.web.controller.order;

import com.bulkpurchase.domain.entity.Cart;
import com.bulkpurchase.domain.entity.CartItem;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.cart.CartItemService;
import com.bulkpurchase.domain.service.cart.CartService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartItemService cartItemService;
    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestParam(value = "productID") Long productID,
                                            @RequestParam(value = "quantity") Integer quantity,
                                            Principal principal) {
        if (principal == null) {
            return createErrorResponse(HttpStatus.UNAUTHORIZED, "사용자 인증이 필요합니다.");
        }

        Optional<Product> productOpt = productService.findById(productID);
        if (productOpt.isEmpty()) {
            return createErrorResponse(HttpStatus.NOT_FOUND, "해당 상품이 존재하지 않습니다.");
        }

        User user = userService.findByUsername(principal.getName());
        Cart cart = cartService.cartFindOrCreate(user);

        // 장바구니에서 해당 상품 검색
        List<CartItem> existingItems = cartItemService.findByCartAndProduct(cart, productOpt.get());

        if (!existingItems.isEmpty()) {
            CartItem existingItem = existingItems.get(0);
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemService.saveCartItem(existingItem);
        } else {
            cartItemService.saveCartItem(new CartItem(cart, productOpt.get(), quantity));
        }
        return ResponseEntity.ok("상품이 장바구니에 추가되었습니다.");
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteCartItem(@RequestParam(value = "itemId") Long itemId, Principal principal) {
        if (principal == null) {
            return createErrorResponse(HttpStatus.UNAUTHORIZED, "사용자 인증이 필요합니다.");
        }

        String username = principal.getName();
        User user = userService.findByUsername(username); // 사용자 이름을 통해 사용자 객체를 가져옵니다.
        if (user == null) {
            return createErrorResponse(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다.");
        }

        Optional<CartItem> cartItemOpt = cartItemService.findById(itemId);
        if (cartItemOpt.isEmpty()) {
            return createErrorResponse(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다.");
        }

        CartItem cartItem = cartItemOpt.get();
        // 사용자의 장바구니와 상품의 장바구니가 일치하는지 확인
        if (!cartItem.getCart().getUser().equals(user)) {
            return createErrorResponse(HttpStatus.FORBIDDEN, "이 상품을 삭제할 권한이 없습니다.");
        }
        cartItemService.deleteCartItemById(itemId);
        return ResponseEntity.ok("상품이 삭제되었습니다.");
    }


    @PostMapping("/deleteSelected")
    public ResponseEntity<?> deleteSelectedItems(@RequestParam(value = "itemIds") List<Long> itemIds, Principal principal) {
        if (principal == null) return createErrorResponse(HttpStatus.UNAUTHORIZED, "사용자 인증이 필요합니다.");

        cartItemService.deleteCartItemsByIds(itemIds);
        return ResponseEntity.ok("선택한 상품들이 삭제되었습니다.");
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateItem(@RequestParam(value = "itemId") Long itemId,
                                                 @RequestParam(value = "quantity") Integer quantity,
                                                 Principal principal) {

        if (principal == null) return createErrorResponse(HttpStatus.UNAUTHORIZED, "사용자 인증이 필요합니다.");

        Optional<CartItem> byId = cartItemService.findById(itemId);
        if (byId.isEmpty()) {
            return createErrorResponse(HttpStatus.NOT_FOUND, "해당 아이템이 존재하지 않습니다.");
        } else {
            CartItem cartItem = byId.get();
            cartItem.setQuantity(quantity);
            cartItemService.saveCartItem(cartItem);
            return ResponseEntity.ok("수정되었습니다.");
        }
    }

    private ResponseEntity<?> createErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(message);
    }
}
