package com.bulkpurchase.web.controller.cart;

import com.bulkpurchase.domain.entity.cart.Cart;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.cart.CartService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
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

    private final CartService cartService;
    private final UserAuthValidator userAuthValidator;

    @GetMapping
    public String cartForm(Principal principal, Model model) {

        User user = userAuthValidator.getCurrentUser(principal);
        Cart cart = cartService.cartFindOrCreate(user);

        model.addAttribute("cart", cart);
        return "order/cart";
    }
}
