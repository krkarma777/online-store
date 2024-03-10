package com.bulkpurchase.domain.dto.inquiry;

import com.bulkpurchase.domain.entity.Inquiry;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InquiryDetailResponseDTO {

    private Long inquiryID;
    private String type;
    private String status;
    private String title;
    private String inquiryContent;
    private String responseContent;
    private LocalDateTime inquiryDate;
    private LocalDateTime responseDate;

    public InquiryDetailResponseDTO() {
    }

    public InquiryDetailResponseDTO(Inquiry inquiry) {
        this.inquiryID = inquiry.getInquiryID();
        this.type = inquiry.getType().getDescription();
        this.status = inquiry.getStatus().getDescription();
        this.title = inquiry.getTitle();
        this.inquiryContent = inquiry.getInquiryContent();
        this.responseContent = inquiry.getResponseContent();
        this.inquiryDate = inquiry.getInquiryDate();
        this.responseDate = inquiry.getResponseDate();
    }
}
