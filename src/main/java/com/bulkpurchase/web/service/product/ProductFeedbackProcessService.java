package com.bulkpurchase.web.service.product;

import com.bulkpurchase.domain.dto.FeedbackRequestDTO;
import com.bulkpurchase.domain.entity.review.Review;
import com.bulkpurchase.domain.entity.review.ReviewFeedback;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.FeedbackType;
import com.bulkpurchase.domain.service.review.ReviewFeedbackService;
import com.bulkpurchase.domain.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductFeedbackProcessService {

    private final ReviewService reviewService;
    private final ReviewFeedbackService reviewFeedbackService;

    public Optional<ResponseEntity<?>> processFeedback(Long reviewID, FeedbackRequestDTO feedbackRequestDTO, User user) {
        try {
            Review review = reviewService.findById(reviewID).orElseThrow(() -> new IllegalArgumentException("Review not found"));
            FeedbackType feedbackType = FeedbackType.valueOf(feedbackRequestDTO.getFeedbackType());
            Date date = new Date();

            ReviewFeedback existingFeedback = reviewFeedbackService.findByReviewIdAndUserId(review, user);
            if (existingFeedback == null) {
                reviewFeedbackService.processFeedback(review, user, feedbackType, date);
            } else {
                existingFeedback.setFeedbackType(feedbackType);
                existingFeedback.setFeedbackDate(date);
                reviewFeedbackService.save(existingFeedback);
            }

            return Optional.of(createResponseEntity(review));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private ResponseEntity<?> createResponseEntity(Review review) {
        long likeCount = reviewFeedbackService.countFeedbackByReviewAndFeedbackType(review, FeedbackType.LIKE);
        long dislikeCount = reviewFeedbackService.countFeedbackByReviewAndFeedbackType(review, FeedbackType.DISLIKE);

        Map<String, Long> response = new HashMap<>();
        response.put("likesCount", likeCount);
        response.put("dislikesCount", dislikeCount);

        return ResponseEntity.ok(response);
    }
}
