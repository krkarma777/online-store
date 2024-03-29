package com.bulkpurchase.domain.repository.review;

import com.bulkpurchase.domain.dto.review.ReviewDetailDTO;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.review.Review;
import com.bulkpurchase.domain.entity.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByUserAndProduct(User user, Product product);
    @Query("SELECT r, " +
            "(SELECT COUNT(f) FROM ReviewFeedback f WHERE f.review = r AND f.feedbackType = 'LIKE') AS likeCount, " +
            "(SELECT COUNT(f) FROM ReviewFeedback f WHERE f.review = r AND f.feedbackType = 'DISLIKE') AS dislikeCount " +
            "FROM Review r WHERE r.product = :product")
    List<Object[]> findReviewDetailsWithFeedbackCountsByProduct(@Param("product") Product product);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.product.productID = :productID")
    long countByProductID(@Param("productID") Long productID);

    // 특정 상품에 대한 리뷰 별점의 평균을 계산
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.productID = :productID")
    Double findAverageRatingByProductID(@Param("productID") Long productID);

    @Query("SELECT new com.bulkpurchase.domain.dto.review.ReviewDetailDTO(r, " +
            "(SELECT COUNT(f) FROM ReviewFeedback f WHERE f.review = r AND f.feedbackType = com.bulkpurchase.domain.enums.FeedbackType.LIKE), " +
            "(SELECT COUNT(f) FROM ReviewFeedback f WHERE f.review = r AND f.feedbackType = com.bulkpurchase.domain.enums.FeedbackType.DISLIKE)) " +
            "FROM Review r WHERE r.reviewID = :reviewID")
    Optional<ReviewDetailDTO> findReviewDetailsWithFeedbackCountsByReviewID(@Param("reviewID") Long reviewID);


    @Query("SELECT r, " +
            "(SELECT COUNT(f) FROM ReviewFeedback f WHERE f.review = r AND f.feedbackType = com.bulkpurchase.domain.enums.FeedbackType.LIKE) AS likeCount, " +
            "(SELECT COUNT(f) FROM ReviewFeedback f WHERE f.review = r AND f.feedbackType = com.bulkpurchase.domain.enums.FeedbackType.DISLIKE) AS dislikeCount " +
            "FROM Review r WHERE r.product.user.userID = :userID " +
            "ORDER BY r.creationDate DESC")
    List<Object[]> findAllReviewDetailsWithFeedbackCountsByUserId(@Param("userID") Long userID, Pageable page);

    @Query("SELECT r, " +
            "(SELECT COUNT(f) FROM ReviewFeedback f WHERE f.review = r AND f.feedbackType = com.bulkpurchase.domain.enums.FeedbackType.LIKE) AS likeCount, " +
            "(SELECT COUNT(f) FROM ReviewFeedback f WHERE f.review = r AND f.feedbackType = com.bulkpurchase.domain.enums.FeedbackType.DISLIKE) AS dislikeCount " +
            "FROM Review r WHERE r.user.userID = :userID " +
            "ORDER BY r.creationDate DESC")
    List<Object[]> findByUser(@Param("userID") Long userID, Pageable page);

    Optional<Review> findByReviewIDAndUser(Long reviewID, User user);
}