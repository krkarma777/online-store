package com.bulkpurchase.domain.dto.inquiry;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryReplyRequestDTO {
    private Long inquiryID;
    private String replyContent;
}