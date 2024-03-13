package com.bulkpurchase.domain.dto.review;

import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.entity.review.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReviewResponseDTO {

    private Long reviewID;
    private String productName;
    private String productImg;
    private String content;
    private Integer rating;
    private LocalDateTime creationDate;
    private List<String> imageUrls;
    private OrderDetail orderDetail;

    public ReviewResponseDTO(Review review) {
        this.reviewID = review.getReviewID();
        this.productName = review.getProduct().getProductName();
        this.productImg = review.getProduct().getImageUrls().get(0);
        this.content = review.getContent();
        this.rating = review.getRating();
        this.creationDate = review.getCreationDate();
        this.imageUrls = review.getImageUrls();
        this.orderDetail = review.getOrderDetail();
    }

    public ReviewResponseDTO() {
    }
}
