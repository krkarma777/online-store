package com.bulkpurchase.domain.dto.inquiry;

import com.bulkpurchase.domain.enums.inquiry.InquiryStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InquiryReplyDTO {

    private Long inquiryID;
    private String responseContent;
    private InquiryStatus status = InquiryStatus.IN_PROGRESS;
    private LocalDateTime responseDate = LocalDateTime.now();
}
