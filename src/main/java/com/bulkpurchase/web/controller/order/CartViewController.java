package com.bulkpurchase.web.controller.order;

import com.bulkpurchase.domain.entity.Cart;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.cart.CartService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartViewController {

    private final UserService userService;
    private final CartService cartService;

    @GetMapping("")
    public String cartForm(Principal principal, Model model) {

        User user = userService.findByUsername(principal.getName());

        Cart cart = cartService.cartFindOrCreate(user);

        model.addAttribute("cart", cart);
        return "order/cart";
    }
}
