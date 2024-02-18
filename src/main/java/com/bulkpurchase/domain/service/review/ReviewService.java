package com.bulkpurchase.domain.service.review;

import com.bulkpurchase.domain.dto.ReviewDetailDTO;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.review.Review;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public void save(Review review) {
        reviewRepository.save(review);
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }


    public Optional<Review> findById(Long reviewID) {
        return reviewRepository.findById(reviewID);
    }

    public Review findByUserAndProduct(User user, Product product) {
        return reviewRepository.findByUserAndProduct(user,product);
    }

    public List<ReviewDetailDTO> reviewDetailsByProduct(Product product) {
        List<Object[]> results = reviewRepository.findReviewDetailsWithFeedbackCountsByProduct(product);
        return results.stream().map(result -> new ReviewDetailDTO(
                (Review) result[0],
                (Long) result[1],
                (Long) result[2]
        )).collect(Collectors.toList());
    }
    public List<ReviewDetailDTO> reviewDetailsByUserID(Long reviewID) {
        List<Object[]> results = reviewRepository.findReviewDetailsWithFeedbackCountsByReviewID(reviewID);
        return results.stream().map(result -> new ReviewDetailDTO(
                (Review) result[0],
                (Long) result[1],
                (Long) result[2]
        )).collect(Collectors.toList());
    }
}
