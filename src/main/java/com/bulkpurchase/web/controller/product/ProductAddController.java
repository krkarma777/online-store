package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.service.product.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductAddController {

    private final CategoryService categoryService;

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("categories", categoryService.findAllWithChildren());
        return "product/productAdd";
    }
}
