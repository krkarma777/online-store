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
        Optional<Product> byId = productService.findById(productID);
        User user = userService.findByUsername(principal.getName()).orElse(null);
        if (byId.isPresent()) {
            Product product = byId.get();
            if (product.getUser().equals(user)) {
                product.setStatus(ProductStatus.INACTIVE);
                productService.saveProduct(product);
                return "redirect:/seller/products";
            } else {
                return "error/403";
            }
        } else {
            return "error/403";
        }
    }

    @GetMapping("/reactivate/{productID}")
    public String reactiveProduct(@PathVariable(value = "productID") Long productID, Principal principal) {
        Optional<Product> byId = productService.findById(productID);
        User user = userService.findByUsername(principal.getName()).orElse(null);
        if (byId.isPresent()) {
            Product product = byId.get();
            if (product.getUser().equals(user)) {
                product.setStatus(ProductStatus.ACTIVE);
                productService.saveProduct(product);
                return "redirect:/seller/products";
            } else {
                return "error/403";
            }
        } else {
            return "error/403";
        }
    }
}
