package com.bulkpurchase.domain.dto.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewWriteRequestDTO {

    private String content;

    private Integer rating;

    private Long productID;

    private Long orderDetailID;

    public ReviewWriteRequestDTO(String content, Integer rating, Long productID, Long orderDetailID) {
        this.content = content;
        this.rating = rating;
        this.productID = productID;
        this.orderDetailID = orderDetailID;
    }

    public ReviewWriteRequestDTO() {
    }
}
