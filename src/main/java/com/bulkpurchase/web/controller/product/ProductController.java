package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.dto.ReviewDetailDTO;
import com.bulkpurchase.domain.entity.product.*;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.review.ReviewFeedbackService;
import com.bulkpurchase.domain.service.review.ReviewService;
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
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final ReviewFeedbackService reviewFeedbackService;
    private final ReviewService reviewService;



    @GetMapping("/product/{productID}")
    public String productDetail(@PathVariable(value = "productID") Long productID, Model model, Principal principal) {
        Optional<Product> productOpt = productService.findById(productID);
        if (productOpt.isEmpty()) {
            //오류
        } else {
            Product product = productOpt.get();
            model.addAttribute("product", product);

            Category category = product.getCategory();
            List<Category> parentCategories = new ArrayList<>();

            while (category != null) {
                parentCategories.add(category);
                category = category.getParent();
            }

            Collections.reverse(parentCategories);
            model.addAttribute("parentCategories", parentCategories);


            List<ReviewDetailDTO> reviews = reviewService.reviewDetailsByProduct(product);
            model.addAttribute("reviews", reviews);

            long reviewCount = reviewService.countByProductID(productID);
            model.addAttribute("reviewCount", reviewCount);

            Double averageRating = reviewService.findAverageRatingByProductID(productID);
            model.addAttribute("averageRating", averageRating);
        }
        return "product/details";
    }


    @GetMapping("/product/list")
    public String showProductList(Model model) {
        List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "product/products";
    }

    @GetMapping("/products/{userId}")
    public String productsByUser(@PathVariable(value = "userId") Long userId, Model model) {
        Optional<User> byUserid = userService.findByUserid(userId);
        User user = null;
        if (byUserid.isPresent()) {
            user = byUserid.get();
        }
        List<Product> products = productService.findByUserOrderByProductIDDesc(user);
        model.addAttribute("products", products);
        return "product/productsByUser";
    }


}
