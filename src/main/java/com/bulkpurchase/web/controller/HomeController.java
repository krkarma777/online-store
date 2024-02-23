package com.bulkpurchase.web.controller;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.enums.ProductStatus;
import com.bulkpurchase.domain.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    
    private final ProductService productService;

    @GetMapping("/*")
    public String goToHome(Model model) {
        ProductStatus status = ProductStatus.ACTIVE;
        List<Product> activeProduct = productService.findActiveProduct(status);
        model.addAttribute("activeProduct", activeProduct);

        List<Product> popularProducts = productService.findPopularProducts();
        model.addAttribute("popularProducts", popularProducts);

        return "home";
    }
}
