package com.bulkpurchase.domain.repository.review;

import com.bulkpurchase.domain.entity.review.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
}
