package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.entity.product.Category;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.product.SaveCheck;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.SalesRegion;
import com.bulkpurchase.domain.service.product.CategoryService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.user.UserService;
import com.bulkpurchase.web.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductAddController {

    private final ProductService productService;
    private final UserAuthValidator userAuthValidator;
    private final CategoryService categoryService;

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        prepareModel(model, new Product());
        return "product/productAdd";
    }

    @PostMapping("/add")
    public String addProduct(@Validated(SaveCheck.class) @ModelAttribute Product product, BindingResult bindingResult, Principal principal,
                             RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            prepareModel(model, product);
            return "product/productAdd";
        }

        product.setUser(userAuthValidator.getCurrentUser(principal));
        Product savedProduct = productService.saveProduct(product);

        redirectAttributes.addAttribute("productId", savedProduct.getProductID()).addAttribute("status", true);
        return "redirect:/product/{productId}";
    }

    private void prepareModel(Model model, Product product) {
        model.addAttribute("product", product);
        model.addAttribute("allSalesRegions", Arrays.asList(SalesRegion.values()));
        model.addAttribute("categories", categoryService.findAllWithChildren());
    }
}
