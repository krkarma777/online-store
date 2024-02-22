package com.bulkpurchase.web.controller.review;

import com.bulkpurchase.domain.dto.FeedbackRequestDTO;
import com.bulkpurchase.domain.entity.review.Review;
import com.bulkpurchase.domain.entity.review.ReviewFeedback;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.FeedbackType;
import com.bulkpurchase.domain.service.review.ReviewFeedbackService;
import com.bulkpurchase.domain.service.review.ReviewService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ReviewFeedbackController {

    private final ReviewFeedbackService reviewFeedbackService;
    private final UserService userService;
    private final ReviewService reviewService;

    @PostMapping("/review/{reviewID}/feedback")
    public ResponseEntity<?> reviewFeedbackUpdate(@PathVariable(value = "reviewID") Long reviewID,
                                                  @RequestBody FeedbackRequestDTO feedbackRequestDTO,
                                                  Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요한 서비스입니다.");
        }

        try {
            User user = userService.findByUsername(principal.getName()).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
            }

            FeedbackType feedbackType = FeedbackType.valueOf(feedbackRequestDTO.getFeedbackType());
            Date date = new Date();
            Review review = reviewService.findById(reviewID).orElse(null);

            ReviewFeedback existingFeedback = reviewFeedbackService.findByReviewIdAndUserId(review, user);
            if (existingFeedback == null) {
                reviewFeedbackService.processFeedback(review, user, feedbackType, date);
            } else {
                existingFeedback.setFeedbackType(feedbackType);
                existingFeedback.setFeedbackDate(date);
                reviewFeedbackService.save(existingFeedback);
            }
            // 업데이트된 좋아요와 싫어요 수 계산
            long likeCount = reviewFeedbackService.countFeedbackByReviewAndFeedbackType(review, FeedbackType.LIKE);
            long dislikeCount = reviewFeedbackService.countFeedbackByReviewAndFeedbackType(review, FeedbackType.DISLIKE);

            // 클라이언트에 반환할 응답 객체 생성
            Map<String, Long> response = new HashMap<>();
            response.put("likesCount", likeCount);
            response.put("dislikesCount", dislikeCount);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing feedback");
        }
    }
}
