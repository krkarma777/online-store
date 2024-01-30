package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.entity.Product;
import com.bulkpurchase.domain.entity.User;
import com.bulkpurchase.domain.enums.SalesRegion;
import com.bulkpurchase.domain.service.ProductService;
import com.bulkpurchase.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    @GetMapping("/product/add")
    public String showRegistrationForm(Model model) {
        model.addAttribute("product", new Product());
        List<SalesRegion> list = Arrays.asList(SalesRegion.values());
        model.addAttribute("allSalesRegions", list);
        return "product/productAdd";
    }

    @PostMapping("/product/add")
    public String addProduct(@Validated @ModelAttribute Product product, BindingResult bindingResult, Principal principal,
                             RedirectAttributes redirectAttributes, Model model) {
        if (principal != null) {
            User currentUser = userService.findByUsername(principal.getName());
            product.setUser(currentUser);
        }

        log.info("product = {}", product);

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
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

    @GetMapping("/product/{productId}")
    public String productDetail(@PathVariable(value = "productId") Long productId, Model model) {
        Optional<Product> product = productService.findById(productId);
        if (product.isEmpty()) {
            //오류
        } else {
            model.addAttribute("product", product.get());
        }
        return "product/details";
    }


    @GetMapping("/product/list")
    public String showProductList(Model model) {
        List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "product/products";
    }

    @GetMapping("/product/edit/{productId}")
    public String editForm(@PathVariable(value = "productId") Long productId, Model model) {
        Optional<Product> product = productService.findById(productId);
        if (product.isEmpty()) {
            //오류
        } else {
            model.addAttribute("product", product.get());
        }
        return "product/edit";
    }

    @PostMapping("/product/edit")
    public String editSave(@ModelAttribute @Validated Product product, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            List<SalesRegion> list = Arrays.asList(SalesRegion.values());
            model.addAttribute("allSalesRegions", list);
            model.addAttribute("product", product);
            return "product/edit/"+product.getProductID();
        }

        Product savedProduct = productService.saveProduct(product);
        redirectAttributes.addAttribute("productId", savedProduct.getProductID());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/product/{productId}";
    }


}
