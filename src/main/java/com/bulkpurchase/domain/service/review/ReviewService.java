package com.bulkpurchase.domain.service.review;

import com.bulkpurchase.domain.dto.review.ReviewDetailDTO;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.review.Review;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    public Optional<Review> findByUserAndProduct(User user, Product product) {
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

    public long countByProductID(Long productID) {
        return reviewRepository.countByProductID(productID);
    }

    public Double findAverageRatingByProductID(Long productID) {
        return reviewRepository.findAverageRatingByProductID(productID);
    }

    public List<ReviewDetailDTO> findAllReviewDetailsWithFeedbackCountsBySeller(Long userID) {
        List<Object[]> results = reviewRepository.findAllReviewDetailsWithFeedbackCountsByUserId(userID);
        return results.stream().map(result -> new ReviewDetailDTO(
                (Review) result[0],
                (Long) result[1],
                (Long) result[2]
        )).collect(Collectors.toList());
    }

    public Page<ReviewDetailDTO> findByUser(User user, Pageable page) {
        List<Object[]> results = reviewRepository.findByUser(user.getUserID(), page);
        List<ReviewDetailDTO> dtos = results.stream()
                .map(result -> new ReviewDetailDTO(
                        (Review) result[0],
                        (Long) result[1],
                        (Long) result[2]
                ))
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, page, dtos.size());
    }
}
