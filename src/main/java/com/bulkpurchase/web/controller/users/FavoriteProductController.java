package com.bulkpurchase.web.controller.users;

import com.bulkpurchase.domain.dto.Response;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.user.FavoriteProductService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorite")
public class FavoriteProductController {

    private final FavoriteProductService favoriteProductService;
    private final ProductService productService;
    private final UserService userService;

    @PostMapping("/toggle")
    public Response toggleFavorite(@RequestParam(value = "productID") Long productID, Principal principal) {
        Product product = productService.findById(productID).orElse(null);
        User user = userService.findByUsername(principal.getName());

        boolean isFavorited = favoriteProductService.toggleFavorite(user, product);
        return new Response(isFavorited);
    }

}

