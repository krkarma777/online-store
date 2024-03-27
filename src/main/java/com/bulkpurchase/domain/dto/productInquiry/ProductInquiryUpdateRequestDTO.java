package com.bulkpurchase.domain.dto.productInquiry;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductInquiryUpdateRequestDTO {

    private Long InquiryID;
    private String inquiryContent;
}
