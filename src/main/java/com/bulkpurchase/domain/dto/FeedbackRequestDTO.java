package com.bulkpurchase.domain.dto;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class FeedbackRequestDTO {
    private String feedbackType;

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }
}
