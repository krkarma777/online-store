package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.user.FeedbackRequestDTO;
import com.bulkpurchase.web.service.product.ProductFeedbackProcessService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ReviewFeedbackController {

    private final ProductFeedbackProcessService productFeedBackProcessService;
    private final UserAuthValidator userAuthValidator;


    @PostMapping("/review/{reviewID}/feedback")
    public ResponseEntity<?> reviewFeedbackUpdate(@PathVariable(value = "reviewID") Long reviewID,
                                                  @RequestBody FeedbackRequestDTO feedbackRequestDTO,
                                                  Principal principal) {
        return userAuthValidator.authenticate(principal)
                .flatMap(user -> productFeedBackProcessService.processFeedback(reviewID, feedbackRequestDTO, user))
                .orElseGet(() -> new ResponseEntity<>("Error processing feedback or user authentication", HttpStatus.BAD_REQUEST));
    }
}
