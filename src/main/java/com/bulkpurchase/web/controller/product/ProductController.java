package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.entity.User;
import com.bulkpurchase.domain.enums.SalesRegion;
import com.bulkpurchase.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import com.bulkpurchase.domain.entity.Product;
import com.bulkpurchase.domain.service.ProductService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;

@Controller
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/product/add")
    public String showRegistrationForm(Model model) {
        model.addAttribute("product", new Product());
        List<SalesRegion> list = Arrays.asList(SalesRegion.values());
        model.addAttribute("allSalesRegions", list);
        return "product/productAdd";
    }

    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute Product product, BindingResult bindingResult, Principal principal,
                             RedirectAttributes redirectAttributes, Model model) {
        if (principal != null) {
            User currentUser = userService.findByUsername(principal.getName());
            product.setUser(currentUser);
        }


        //검증 로직
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "productName","required");

        if (product.getPrice() == null || product.getPrice() < 1000 || product.getPrice() > 1000000) {
            bindingResult.rejectValue("price","range", new Object[]{1000,100000},null);
        }
        if (!StringUtils.hasText(product.getDescription())) {
            bindingResult.rejectValue("description","required");
        }
        if (product.getSalesRegions().isEmpty()) {
            bindingResult.rejectValue("salesRegions","required");
        }
        if (product.getStock() == null || product.getStock() > 9999) {
            bindingResult.rejectValue("stock","max", new Object[]{99999},null);
        }
        //특정 필드가 아닌 복합 룰 검증
        if (product.getPrice() != null && product.getStock() != null) {
            long resultPrice = product.getPrice() * product.getStock();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice},null);
                bindingResult.addError(new ObjectError("product", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재값 = " + resultPrice));
            }
        }
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
}
