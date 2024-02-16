package com.bulkpurchase.domain.repository.review;

import com.bulkpurchase.domain.entity.review.ReviewDislike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewDislikeRepository  extends JpaRepository<ReviewDislike, Long> {
}
