package com.bulkpurchase.domain.entity.product;

import com.bulkpurchase.domain.dto.productInquiry.ProductInquiryCreateRequestDTO;
import com.bulkpurchase.domain.dto.productInquiry.ProductInquiryReplyRequestDTO;
import com.bulkpurchase.domain.dto.productInquiry.ProductInquiryUpdateRequestDTO;
import com.bulkpurchase.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "ProductInquiries")
@Getter
@Setter
public class ProductInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productID", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    @Column(nullable = false, length = 500)
    private String inquiryContent;

    @Column(length = 500)
    private String replyContent;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inquiryDate = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date replyDate;

    public ProductInquiry(ProductInquiryCreateRequestDTO requestDTO, Product product, User user) {
        this.product = product;
        this.inquiryContent = requestDTO.getInquiryContent();
        this.user = user;
    }

    public ProductInquiry() {
    }

    public void update(ProductInquiryUpdateRequestDTO requestDTO) {
        this.inquiryContent = requestDTO.getInquiryContent();
    }

    public void reply(ProductInquiryReplyRequestDTO requestDTO) {
        this.replyContent = requestDTO.getReplyContent();
        this.replyDate = new Date();
    }
}
