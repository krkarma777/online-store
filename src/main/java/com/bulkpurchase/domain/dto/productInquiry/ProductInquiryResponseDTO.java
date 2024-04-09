package com.bulkpurchase.domain.dto.productInquiry;

import com.bulkpurchase.domain.dto.product.ProductResponseDTO;
import com.bulkpurchase.domain.dto.user.UserResponseDTO;
import com.bulkpurchase.domain.entity.product.ProductInquiry;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Optional;

@Getter
@Setter
public class ProductInquiryResponseDTO {

    private Long inquiryID;

    private ProductResponseDTO product;

    private UserResponseDTO user;

    private String inquiryContent;

    private String replyContent;

    private String inquiryDate;

    private String replyDate;

    public ProductInquiryResponseDTO(ProductInquiry productInquiry) {
        this.inquiryID = productInquiry.getInquiryID();
        this.product = new ProductResponseDTO(productInquiry.getProduct());
        this.user = new UserResponseDTO(productInquiry.getUser());
        this.inquiryContent = productInquiry.getInquiryContent();
        this.replyContent = productInquiry.getReplyContent();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        this.inquiryDate = Optional.ofNullable(productInquiry.getInquiryDate()).map(formatter::format).orElse(null);
        this.replyDate = Optional.ofNullable(productInquiry.getReplyDate()).map(formatter::format).orElse(null);
    }

    public ProductInquiryResponseDTO() {
    }
}
