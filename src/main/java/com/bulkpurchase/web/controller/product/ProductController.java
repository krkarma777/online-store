package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.service.product.CategoryService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final CategoryService categoryService;
    private final UserAuthValidator userAuthValidator;

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("categories", categoryService.findAllWithChildren());
        return "product/productAdd";
    }

    @GetMapping("/update/{productID}")
    public String editForm(@PathVariable("productID") Long productID, Model model) {
        model.addAttribute("categories", categoryService.findAllWithChildren());
        model.addAttribute("productID", productID);
        return "product/update";
    }

    @GetMapping("/seller")
    public String manageProducts(Principal principal, Model model) {
        Long userID = userAuthValidator.getCurrentUser(principal).getUserID();
        model.addAttribute("sellerID", userID);
        return "/seller/productManage/products";
    }
}
