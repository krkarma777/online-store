package com.bulkpurchase.web.controller.review;

import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.review.Review;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.review.ReviewService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class ReviewFormController {

    private final ProductService productService;
    private final UserAuthValidator userAuthValidator;
    private final OrderDetailService orderDetailService;
    private final ReviewService reviewService;

    @GetMapping("/review/write")
    public String reviewWriteForm(@RequestParam("productID") Long productID,
                                  @RequestParam("orderDetailID") Long orderDetailID,
                                  Principal principal, Model model) {
        User user = userAuthValidator.getCurrentUser(principal);
        Product product = productService.findById(productID)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        OrderDetail orderDetail = orderDetailService.findByID(orderDetailID)
                .orElseThrow(() -> new IllegalArgumentException("Order detail not found"));

        if (!orderDetail.getProduct().equals(product)) {
            return "error/403";
        }
        Optional<Review> existingReview = reviewService.findByUserAndProduct(user, product);
        if (existingReview.isPresent()) {
            return "redirect:/review/" + existingReview.get().getReviewID();
        } else {
            model.addAttribute("review", new Review());
            return "review/reviewForm";
        }
    }
}