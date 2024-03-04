package com.bulkpurchase.web.controller.order;

import com.bulkpurchase.domain.entity.cart.Cart;
import com.bulkpurchase.domain.entity.cart.CartItem;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.service.cart.CartService;
import com.bulkpurchase.domain.service.product.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class CartToOrderController {

    private final ProductService productService;
    private final CartService cartService;

    @Value("${paypal.client.id}")
    private String paypalClientId;

    @GetMapping("/{cartID}")
    public String orderForm(@PathVariable(value = "cartID") Long cartID, Model model, HttpServletResponse response, @RequestParam("itemId") List<Long> itemIds) {
        Optional<Cart> cartOpt = cartService.findById(cartID);
        if (cartOpt.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 에러 설정
            return "error/400";
        }
        Cart cart = cartOpt.get();
        Set<CartItem> items = cart.getItems();
        List<CartItem> buyItems = new ArrayList<>();
        Iterator<CartItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            for (Long itemId : itemIds) {
                if (item.getCartItemID().equals(itemId)) {
                    buyItems.add(item);
                    iterator.remove();
                }
            }
        }
        cart.setItems(items);
        cartService.save(cart);

        model.addAttribute("paypalClientId", paypalClientId);

        model.addAttribute("items", buyItems);
        return "order/orderForm";
    }

    @PostMapping("/item/one")
    public String oneItemOrder(@RequestParam("productID") Long productID,
                               @RequestParam("quantity") Integer quantity,
                               Model model) {
        List<CartItem> buyItems = new ArrayList<>();
        Product product = productService.findById(productID).orElse(null);
        if (product == null) {
            return "error/403";
        }
        Cart cart = new Cart();
        CartItem cartItem = new CartItem(cart, product, quantity);

        buyItems.add(cartItem);
        model.addAttribute("items", buyItems);

        model.addAttribute("paypalClientId", paypalClientId);
        return "order/orderForm";
    }
}
