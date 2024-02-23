package com.bulkpurchase.domain.dto.review;

import com.bulkpurchase.domain.entity.review.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDetailDTO {
    private Review review;
    private long likeCount;
    private long dislikeCount;

    public ReviewDetailDTO(Review review, long likeCount, long dislikeCount) {
        this.review = review;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
    }
}
