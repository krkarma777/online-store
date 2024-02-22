package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.dto.ReviewDetailDTO;
import com.bulkpurchase.domain.entity.product.*;
import com.bulkpurchase.domain.entity.user.FavoriteProduct;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.product.ProductInquiryService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.review.ReviewFeedbackService;
import com.bulkpurchase.domain.service.review.ReviewService;
import com.bulkpurchase.domain.service.user.FavoriteProductService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final ReviewService reviewService;
    private final ProductInquiryService productInquiryService;
    private final FavoriteProductService favoriteProductService;

    @GetMapping("/{productID}")
    public String productDetail(@PathVariable("productID") Long productID, Model model, Principal principal) {
        System.out.println("productID = " + productID);
        return productService.findById(productID)
                .map(product -> {
                    populateProductDetails(model, principal, product);
                    return "product/details";
                })
                .orElse("redirect:/error"); // 예제 에러 페이지 리디렉션
    }

    private void populateProductDetails(Model model, Principal principal, Product product) {
        model.addAttribute("product", product);
        model.addAttribute("parentCategories", getParentCategories(product.getCategory()));
        model.addAttribute("reviews", reviewService.reviewDetailsByProduct(product));
        model.addAttribute("reviewCount", reviewService.countByProductID(product.getProductID()));
        model.addAttribute("averageRating", reviewService.findAverageRatingByProductID(product.getProductID()));
        model.addAttribute("inquiries", productInquiryService.findByProductProductID(product.getProductID()));
        if (principal != null) {
            userService.findByUsername(principal.getName()).ifPresent(user -> {
                model.addAttribute("favoriteProduct", favoriteProductService.findByUserAndProduct(user, product));
                model.addAttribute("isSeller", product.getUser().equals(user));
            });
        }
    }

    private List<Category> getParentCategories(Category category) {
        List<Category> parentCategories = new ArrayList<>();
        while (category != null) {
            parentCategories.add(0, category);
            category = category.getParent();
        }
        return parentCategories;
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
