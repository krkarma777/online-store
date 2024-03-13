package com.bulkpurchase.domain.dto.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewUpdateRequestDTO {

    private String content;

    private Integer rating;
}
