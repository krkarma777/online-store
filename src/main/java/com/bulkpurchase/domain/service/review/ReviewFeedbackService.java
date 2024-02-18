package com.bulkpurchase.domain.service.review;

import com.bulkpurchase.domain.entity.review.Review;
import com.bulkpurchase.domain.entity.review.ReviewFeedback;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.FeedbackType;
import com.bulkpurchase.domain.repository.review.ReviewFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ReviewFeedbackService {

    private final ReviewFeedbackRepository reviewFeedbackRepository;

    public void save(ReviewFeedback reviewFeedback) {
        reviewFeedbackRepository.save(reviewFeedback);
    }

    public ReviewFeedback findByReviewIdAndUserId(Review review, User user) {
        return reviewFeedbackRepository.findByReviewAndUser(review, user);
    }

    public long countFeedbackByReviewAndFeedbackType(Review review, FeedbackType feedbackType) {
        return reviewFeedbackRepository.countFeedbackByReviewAndFeedbackType(review, feedbackType);
    }

    public void processFeedback(Review review, User user, FeedbackType feedbackType, Date date) {
        ReviewFeedback reviewFeedback = new ReviewFeedback();
        reviewFeedback.setFeedbackDate(date);
        reviewFeedback.setFeedbackType(feedbackType);
        reviewFeedback.setUser(user);
        reviewFeedback.setReview(review);
        reviewFeedbackRepository.save(reviewFeedback);
    }
}
