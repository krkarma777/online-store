package com.bulkpurchase.domain.dto.productInquiry;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductInquiryReplyRequestDTO {
    private Long inquiryID;
    private String replyContent;
}