package com.bulkpurchase.domain.dto.inquiry;

import com.bulkpurchase.domain.entity.Inquiry;
import lombok.Getter;
import lombok.Setter;

import static com.bulkpurchase.CustomTimeFormatter.timeFormat;

@Getter
@Setter
public class InquiryDetailResponseDTO {

    private Long inquiryID;
    private String type;
    private String status;
    private String title;
    private String inquiryContent;
    private String responseContent;
    private String inquiryDate;
    private String responseDate;

    public InquiryDetailResponseDTO() {
    }

    public InquiryDetailResponseDTO(Inquiry inquiry) {
        this.inquiryID = inquiry.getInquiryID();
        this.type = inquiry.getType().getDescription();
        this.status = inquiry.getStatus().getDescription();
        this.title = inquiry.getTitle();
        this.inquiryContent = inquiry.getInquiryContent();
        this.responseContent = inquiry.getResponseContent();
        this.inquiryDate = timeFormat(inquiry.getInquiryDate());
        this.responseDate = timeFormat(inquiry.getResponseDate());
    }
}
