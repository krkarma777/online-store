package com.bulkpurchase.web.controller.order;

import com.bulkpurchase.domain.entity.Cart;
import com.bulkpurchase.domain.entity.CartItem;
import com.bulkpurchase.domain.entity.User;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.service.ProductService;
import com.bulkpurchase.domain.service.cart.CartItemService;
import com.bulkpurchase.domain.service.cart.CartService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartItemService cartItemService;
    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService;

    @PostMapping("/cart/add")
    @ResponseBody
    public ResponseEntity<?> addToCart(@RequestParam(value = "productID") Long productID,
                                       @RequestParam(value = "quantity") Integer quantity, Principal principal) {

        try {
            return processAddToCart(productID, quantity, principal);
        } catch (Exception e) {
            // 로그 기록 추가
            // 예를 들면, Logger.error("Error adding product to cart: ", e);
            return createErrorResponse("상품 추가 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/cart")
    public String cartForm(Principal principal, Model model) {
        User currentUser = userService.findByUsername(principal.getName());
        Cart cart = cartService.cartFindOrCreate(currentUser);
        model.addAttribute("cart", cart);
        return "/order/cart";
    }


    private ResponseEntity<?> processAddToCart(Long productID, Integer quantity, Principal principal) {

        Optional<Product> productOpt = productService.findById(productID);
        if (productOpt.isEmpty()) {
            return createErrorResponse("해당 상품이 존재하지 않습니다.");
        }

        if (principal == null) {
            return createErrorResponse("사용자 인증이 필요합니다.");
        }

        CartItem item = createCartItem(productOpt.get(), quantity, principal);
        cartItemService.saveCartItem(item);
        return ResponseEntity.ok().body("상품이 장바구니에 추가되었습니다.");
    }

    private CartItem createCartItem(Product product, Integer quantity, Principal principal) {

        User currentUser = userService.findByUsername(principal.getName());
        Cart cart = cartService.cartFindOrCreate(currentUser);

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setCart(cart);

        return item;
    }

    private ResponseEntity<?> createErrorResponse(String message) {

        return ResponseEntity.badRequest().body(message);
    }
}
