package com.bulkpurchase.domain.service.product;

import com.bulkpurchase.domain.entity.product.ProductInquiry;
import com.bulkpurchase.domain.repository.product.ProductInquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductInquiryService {

    private final ProductInquiryRepository productInquiryRepository;

    public List<ProductInquiry> findByProductProductID(Long productID) {
        return productInquiryRepository.findByProductProductID(productID);
    }

    public void save(ProductInquiry productInquiry) {
        productInquiryRepository.save(productInquiry);
    }

    public Optional<ProductInquiry> findById(Long inquiryID) {
        return productInquiryRepository.findById(inquiryID);
    }
}
