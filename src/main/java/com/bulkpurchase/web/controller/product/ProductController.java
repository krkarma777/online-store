package com.bulkpurchase.web.controller.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import com.bulkpurchase.domain.entity.ProductEntity;
import com.bulkpurchase.domain.service.ProductService;

import java.util.List;

@Controller
@Slf4j
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/productAdd")
    public String showRegistrationForm(Model model) {
        model.addAttribute("product", new ProductEntity());
        return "product/productAdd";
    }

    @PostMapping("/productAdd")
    public String registerProduct(@ModelAttribute ProductEntity product) {
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/products")
    public String showProductList(Model model) {
        List<ProductEntity> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "product/products";
    }
}
