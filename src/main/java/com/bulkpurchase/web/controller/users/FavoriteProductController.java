package com.bulkpurchase.web.controller.users;

import com.bulkpurchase.domain.dto.user.FavoriteResponse;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.FavoriteProduct;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.user.FavoriteProductService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/favorite")
@Controller
public class FavoriteProductController {

    private final FavoriteProductService favoriteProductService;
    private final ProductService productService;
    private final UserAuthValidator userAuthValidator;

    @PostMapping("/toggle")
    @ResponseBody
    public FavoriteResponse toggleFavorite(@RequestParam(value = "productID") Long productID, Principal principal) {
        Product product = productService.findById(productID).orElse(null);
        User user = userAuthValidator.getCurrentUser(principal);
        boolean isFavorited = favoriteProductService.toggleFavorite(user, product);
        return new FavoriteResponse(isFavorited);
    }

    @PostMapping("/toggle/list")
    @ResponseBody
    public FavoriteResponse toggleFavoriteList(@RequestParam(value = "productIDs") List<Long> productIDs, Principal principal) {
        boolean isFavorited = true;
        for (Long productID : productIDs) {
            Product product = productService.findById(productID).orElse(null);
            User user = userAuthValidator.getCurrentUser(principal);
            boolean toggleFavorite = favoriteProductService.toggleFavorite(user, product);
            if (!toggleFavorite) {
                isFavorited = toggleFavorite;
            }
        }

        return new FavoriteResponse(isFavorited);
    }

}

