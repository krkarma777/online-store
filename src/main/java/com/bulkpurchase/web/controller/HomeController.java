package com.bulkpurchase.web.controller;

import com.bulkpurchase.domain.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ProductService productService;

    @GetMapping("/*")
    public String goToHome(Model model){
        productService.findAllProducts();

        return "home";
    }

}
