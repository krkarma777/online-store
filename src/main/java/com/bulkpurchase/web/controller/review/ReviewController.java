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

import java.security.Principal;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class ReviewController {

    private final ReviewService reviewService;
    private final ProductService productService;
    private final UserService userService;
    private final OrderDetailService orderDetailService;

    @GetMapping("/review/write")
    public String reviewWriteForm(@RequestParam(value = "productID") Long productID,
                                  @RequestParam(value = "orderDetailID") Long orderDetailID,
                                  Principal principal, Model model) {

        User user = userService.findByUsername(principal.getName()).orElse(null);
        Product product = productService.findById(productID).orElse(null);
        OrderDetail orderDetail = orderDetailService.findByID(orderDetailID).orElse(null);

        if (orderDetail == null || !orderDetail.getProduct().equals(product)) {
            return "error/403";
        }

        Review existingReview = reviewService.findByUserAndProduct(user, product);
        if (existingReview != null) {
            return "redirect:/review/" + existingReview.getReviewID();
        }

        Review review = new Review();
        model.addAttribute("review", review);
        return "review/reviewForm";
    }


    @PostMapping("/review/write")
    public String reviewWrite(@ModelAttribute Review review, Principal principal,
                              @RequestParam(value = "productID") Long productID,
                              @RequestParam(value = "orderDetailID") Long orderDetailID) {
        User user = userService.findByUsername(principal.getName()).orElse(null);
        review.setUser(user);
        Product product = productService.findById(productID).orElse(null);
        OrderDetail orderDetail = orderDetailService.findByID(orderDetailID).orElse(null);

        review.setProduct(product);
        review.setOrderDetail(orderDetail);

        Date date = new Date();
        review.setCreationDate(date);

        reviewService.save(review);
        return "redirect:"+review.getReviewID();
    }

    @GetMapping("/review/{reviewID}")
    private String reviewDetail(@PathVariable("reviewID") Long reviewID, Model model ) {
        List<ReviewDetailDTO> reviews = reviewService.reviewDetailsByUserID(reviewID);
        model.addAttribute("reviews", reviews);
        return "review/reviewDetail";
    }
}
