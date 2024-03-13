package com.bulkpurchase.web.controller.users.myPage.active;

import com.bulkpurchase.domain.dto.review.ReviewDetailDTO;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.review.ReviewService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class MyPageReviewsController {

    private final ReviewService reviewService;
    private final UserAuthValidator userAuthValidator;

    @GetMapping
    public String myPageReviews(Model model, Principal principal, @RequestParam(value = "page", required = false) Integer pageNum) {
        if (pageNum == null) {
            pageNum = 0;
        }
        User user = userAuthValidator.getCurrentUser(principal);
        Pageable page = PageRequest.of(pageNum, 3);
        Page<ReviewDetailDTO> reviewDetailDTOS = reviewService.findByUser(user, page);

        int totalPages = reviewDetailDTOS.getTotalPages();
        model.addAttribute("reviewDetailDTOS", reviewDetailDTOS);
        model.addAttribute("totalPages", totalPages);
        return "users/myPage/active/review/reviews";
    }
}
