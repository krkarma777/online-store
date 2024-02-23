package com.bulkpurchase.web.controller.seller;

import com.bulkpurchase.domain.dto.ReviewDetailDTO;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.review.ReviewService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReviewManageController {

    private final UserService userService;
    private final ReviewService reviewService;

    @GetMapping("/seller/reviews")
    public String reviewManage(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName()).orElse(null);
        List<ReviewDetailDTO> reviews = reviewService.findAllReviewDetailsWithFeedbackCountsBySeller(user.getUserID());
        model.addAttribute("reviews", reviews);
        return "/seller/productManage/reviews";
    }
}
