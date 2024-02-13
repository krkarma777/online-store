package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.entity.product.Category;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.product.SaveCheck;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.SalesRegion;
import com.bulkpurchase.domain.service.product.CategoryService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.user.UserService;
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
    private final UserService userService;
    private final CategoryService categoryService;


    @GetMapping("/add")
    public String showRegistrationForm(Model model) {
        model.addAttribute("product", new Product());
        List<SalesRegion> list = Arrays.asList(SalesRegion.values());
        model.addAttribute("allSalesRegions", list);
        List<Category> categories = categoryService.findAllWithChildren();
        model.addAttribute("categories", categories);
        return "product/productAdd";
    }

    @PostMapping("/add")
    public String addProduct(@Validated(SaveCheck.class) @ModelAttribute Product product, BindingResult bindingResult, Principal principal,
                             RedirectAttributes redirectAttributes, Model model) {
        if (principal != null) {
            User currentUser = userService.findByUsername(principal.getName());
            product.setUser(currentUser);
        }


        List<Category> categories = categoryService.findAllWithChildren();
        model.addAttribute("categories", categories);
        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            List<SalesRegion> list = Arrays.asList(SalesRegion.values());
            model.addAttribute("allSalesRegions", list);
            model.addAttribute("product", product);
            return "product/productAdd";
        }

        Product savedProduct = productService.saveProduct(product);
        redirectAttributes.addAttribute("productId", savedProduct.getProductID());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/product/{productId}";

    }
}
