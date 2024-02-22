package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.ProductStatus;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductStatusController {

    private final ProductService productService;
    private final UserService userService;

    @GetMapping("/delete/{productID}")
    public String deleteProduct(@PathVariable(value = "productID") Long productID, Principal principal) {
        return updateProductStatus(productID, principal, ProductStatus.INACTIVE);
    }

    @GetMapping("/reactivate/{productID}")
    public String reactiveProduct(@PathVariable(value = "productID") Long productID, Principal principal) {
        return updateProductStatus(productID, principal, ProductStatus.ACTIVE);
    }

    private String updateProductStatus(Long productID, Principal principal, ProductStatus status) {
        return productService.findById(productID)
                .filter(product -> isOwner(principal, product))
                .map(product -> {
                    product.setStatus(status);
                    productService.saveProduct(product);
                    return "redirect:/seller/products";
                })
                .orElse("error/403");
    }

    private boolean isOwner(Principal principal, Product product) {
        return userService.findByUsername(principal.getName())
                .map(user -> product.getUser().equals(user))
                .orElse(false);
    }
}
