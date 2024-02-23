package com.bulkpurchase.web.controller.admin;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminProductManageController {

    private final ProductService productService;

    @GetMapping("/product")
    public String productManagementPage(Model model) {
        List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);

        return "/admin/productManagement";
    }
}
