package com.bulkpurchase.web.controller.order;

import com.bulkpurchase.domain.entity.cart.Cart;
import com.bulkpurchase.domain.entity.cart.CartItem;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class CartToOrderController {

    private final ProductService productService;
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

        return "order/orderForm";
    }
}
