package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.entity.product.ProductInquiry;
import com.bulkpurchase.domain.service.product.ProductInquiryService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.user.UserService;
import com.bulkpurchase.web.service.product.ProductDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final ProductInquiryService productInquiryService;
    private final ProductDetailService productDetailService;

    @GetMapping("/{productID}")
    public String productDetail(@PathVariable("productID") Long productID, Model model, Principal principal) {
        return productService.findById(productID)
                .map(product -> {
                    productDetailService.populateProductDetails(model, principal, product);
                    return "product/details";
                })
                .orElse("redirect:/error"); // 예제 에러 페이지 리디렉션
    }


    @GetMapping("/list")
    public String showProductList(Model model) {
        model.addAttribute("products", productService.findAllProducts());
        return "product/products";
    }

    @GetMapping("/user/{userId}")
    public String productsByUser(@PathVariable("userId") Long userId, Model model) {
        userService.findByUserid(userId)
                .ifPresent(user -> model.addAttribute("products", productService.findByUserOrderByProductIDDesc(user)));
        return "product/productsByUser";
    }

    @GetMapping("/{productID}/inquiry")
    public String productInquiry(@PathVariable("productID") Long productID, Model model) {
        model.addAttribute("inquiries", productInquiryService.findByProductProductID(productID));
        model.addAttribute("productInquiry", new ProductInquiry());
        return "product/ProductInquiry";
    }
}
