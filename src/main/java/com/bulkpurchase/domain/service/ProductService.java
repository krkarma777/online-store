package com.bulkpurchase.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bulkpurchase.domain.entity.ProductEntity;
import com.bulkpurchase.domain.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductEntity saveProduct(ProductEntity product) {
        return productRepository.save(product);
    }

    public List<ProductEntity> findAllProducts() {
        return productRepository.findAll();
    }
}
