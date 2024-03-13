package com.bulkpurchase.domain.dto.review;

import com.bulkpurchase.domain.entity.review.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDetailDTO {
    private ReviewResponseDTO reviewResponseDTO;
    private long likeCount;
    private long dislikeCount;

    public ReviewDetailDTO(Review review, long likeCount, long dislikeCount) {
        this.reviewResponseDTO = new ReviewResponseDTO(review);
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
    }
}
