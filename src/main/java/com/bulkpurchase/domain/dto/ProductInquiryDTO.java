package com.bulkpurchase.domain.dto;

import com.bulkpurchase.domain.entity.product.ProductInquiry;
import lombok.Getter;
import lombok.Setter;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ProductInquiryDTO {
    private Long inquiryID;
    private Long productID; // 상품 ID만 포함
    private String productName;
    private Long userID; // 사용자 ID만 포함
    private String realName;
    private String inquiryContent;
    private String replyContent;
    private String inquiryDate;
    private String replyDate;

    public ProductInquiryDTO(ProductInquiry inquiry) {
        this.inquiryID = inquiry.getInquiryID();
        this.productID = inquiry.getProduct().getProductID();
        this.productName = inquiry.getProduct().getProductName();
        this.userID = inquiry.getUser().getUserID();
        this.realName = inquiry.getUser().getRealName();
        this.inquiryContent = inquiry.getInquiryContent();
        this.replyContent = inquiry.getReplyContent();
        if (inquiry.getInquiryDate() != null) {
            this.inquiryDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(inquiry.getInquiryDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }

        if (inquiry.getReplyDate() != null) {
            this.replyDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(inquiry.getReplyDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
    }

}
