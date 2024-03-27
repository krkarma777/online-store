package com.bulkpurchase.domain.service.product;

import com.bulkpurchase.domain.dto.product.ProductInquiryDTO;
import com.bulkpurchase.domain.entity.product.ProductInquiry;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.repository.product.ProductInquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductInquiryService {

    private final ProductInquiryRepository productInquiryRepository;

    public List<ProductInquiry> findByProductProductID(Long productID) {
        return productInquiryRepository.findByProductProductID(productID);
    }
    public List<ProductInquiry> findByProductUser(User user) {
        return productInquiryRepository.findByProductUser(user);
    }

    public ProductInquiry save(ProductInquiry productInquiry) {
        return productInquiryRepository.save(productInquiry);
    }

    public Optional<ProductInquiry> findById(Long inquiryID) {
        return productInquiryRepository.findById(inquiryID);
    }

    public ProductInquiry replyToInquiry(Long inquiryID, String replyContent) {
        ProductInquiry productInquiry = productInquiryRepository.findById(inquiryID).orElse(null);
        if (productInquiry == null) {
            return null;
        }
        productInquiry.setReplyDate(new Date());
        productInquiry.setReplyContent(replyContent);
        return productInquiryRepository.save(productInquiry);
    }


    public Page<ProductInquiryDTO> findAllDTO(Pageable pageable) {
        Page<ProductInquiry> inquiriesPage = productInquiryRepository.findAll(pageable);
        return inquiriesPage.map(ProductInquiryDTO::new);
    }
}
