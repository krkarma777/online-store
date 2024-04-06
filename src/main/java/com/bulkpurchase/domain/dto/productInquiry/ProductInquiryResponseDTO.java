package com.bulkpurchase.domain.dto.productInquiry;

import com.bulkpurchase.domain.dto.product.ProductResponseDTO;
import com.bulkpurchase.domain.dto.user.UserResponseDTO;
import com.bulkpurchase.domain.entity.product.ProductInquiry;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProductInquiryResponseDTO {

    private Long inquiryID;

    private ProductResponseDTO product;

    private UserResponseDTO user;

    private String inquiryContent;

    private String replyContent;

    private Date inquiryDate;

    private Date replyDate;

    public ProductInquiryResponseDTO(ProductInquiry productInquiry) {
        this.inquiryID = productInquiry.getInquiryID();
        this.product = new ProductResponseDTO(productInquiry.getProduct());
        this.user = new UserResponseDTO(productInquiry.getUser());
        this.inquiryContent = productInquiry.getInquiryContent();
        this.replyContent = productInquiry.getReplyContent();
        this.inquiryDate = productInquiry.getInquiryDate();
        this.replyDate = productInquiry.getReplyDate();
    }

    public ProductInquiryResponseDTO() {
    }
}
