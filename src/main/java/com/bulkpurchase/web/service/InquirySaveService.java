package com.bulkpurchase.web.service;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.product.ProductInquiry;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.product.ProductInquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class InquirySaveService {

    private final ProductInquiryService productInquiryService;

    public boolean saveInquiryAnswer(Long inquiryID, String replyContent) {
        return productInquiryService.findById(inquiryID).map(inquiry -> {
            inquiry.setReplyContent(replyContent);
            inquiry.setReplyDate(new Date());
            productInquiryService.save(inquiry);
            return true;
        }).orElse(false);
    }

    public void saveInquiry(ProductInquiry inquiry, User user, Product product) {
        inquiry.setUser(user);
        inquiry.setProduct(product);
        productInquiryService.save(inquiry);
    }
}
