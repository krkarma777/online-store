package com.bulkpurchase.domain.repository.review;

import com.bulkpurchase.domain.entity.review.Review;
import com.bulkpurchase.domain.entity.review.ReviewFeedback;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.FeedbackType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewFeedbackRepository extends JpaRepository<ReviewFeedback, Long> {
    ReviewFeedback findByReviewAndUser(Review review, User user);

    long countFeedbackByReviewAndFeedbackType(Review review, FeedbackType feedbackType);
}
