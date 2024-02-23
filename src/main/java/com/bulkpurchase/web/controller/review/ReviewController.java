package com.bulkpurchase.web.controller.review;

import com.bulkpurchase.domain.dto.ReviewDetailDTO;
import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.review.Review;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.review.ReviewService;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class ReviewController {

    private final ReviewService reviewService;
    private final ProductService productService;
    private final UserService userService;
    private final OrderDetailService orderDetailService;

    @GetMapping("/review/write")
    public String reviewWriteForm(@RequestParam("productID") Long productID,
                                  @RequestParam("orderDetailID") Long orderDetailID,
                                  Principal principal, Model model) {
        User user = getCurrentUser(principal);
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


    @PostMapping("/review/write")
    public String reviewWrite(@ModelAttribute Review review, Principal principal,
                              @RequestParam("productID") Long productID,
                              @RequestParam("orderDetailID") Long orderDetailID,
                              RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(principal);
        Product product = productService.findById(productID)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        OrderDetail orderDetail = orderDetailService.findByID(orderDetailID)
                .orElseThrow(() -> new IllegalArgumentException("Order detail not found"));

        review.setUser(user);
        review.setProduct(product);
        review.setOrderDetail(orderDetail);
        review.setCreationDate(new Date());

        reviewService.save(review);

        redirectAttributes.addFlashAttribute("reviewID", review.getReviewID());
        return "redirect:/review/" + review.getReviewID();
    }

    @GetMapping("/review/{reviewID}")
    public String reviewDetail(@PathVariable("reviewID") Long reviewID, Model model) {
        List<ReviewDetailDTO> reviews = reviewService.reviewDetailsByUserID(reviewID);
        model.addAttribute("reviews", reviews);
        return "review/reviewDetail";
    }

    private User getCurrentUser(Principal principal) {
        return userService.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
