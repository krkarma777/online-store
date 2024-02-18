package com.bulkpurchase.domain.repository.review;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.review.Review;
import com.bulkpurchase.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findByUserAndProduct(User user, Product product);
    @Query("SELECT r, " +
            "(SELECT COUNT(f) FROM ReviewFeedback f WHERE f.review = r AND f.feedbackType = 'LIKE') AS likeCount, " +
            "(SELECT COUNT(f) FROM ReviewFeedback f WHERE f.review = r AND f.feedbackType = 'DISLIKE') AS dislikeCount " +
            "FROM Review r WHERE r.product = :product")
    List<Object[]> findReviewDetailsWithFeedbackCountsByProduct(@Param("product") Product product);

    @Query("SELECT r, " +
            "(SELECT COUNT(f) FROM ReviewFeedback f WHERE f.review = r AND f.feedbackType = com.bulkpurchase.domain.enums.FeedbackType.LIKE) AS likeCount, " +
            "(SELECT COUNT(f) FROM ReviewFeedback f WHERE f.review = r AND f.feedbackType = com.bulkpurchase.domain.enums.FeedbackType.DISLIKE) AS dislikeCount " +
            "FROM Review r WHERE r.reviewID = :reviewID")
    List<Object[]> findReviewDetailsWithFeedbackCountsByReviewID(@Param("reviewID") Long reviewID);
}