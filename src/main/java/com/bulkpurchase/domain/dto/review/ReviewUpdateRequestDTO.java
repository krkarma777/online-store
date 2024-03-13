package com.bulkpurchase.domain.dto.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewUpdateRequestDTO {

    private String content;
    private Integer rating;

    public ReviewUpdateRequestDTO(String content, Integer rating) {
        this.content = content;
        this.rating = rating;
    }

    public ReviewUpdateRequestDTO() {
    }
}
