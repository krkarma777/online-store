package com.bulkpurchase.domain.dto.inquiry;

import com.bulkpurchase.domain.enums.inquiry.InquiryType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InquiryCreateRequestDTO {

    private String title;
    private String inquiryContent;
    private InquiryType inquiryType;
}
