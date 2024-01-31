package com.bulkpurchase.domain.service;

import com.bulkpurchase.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long productId) {
        return productRepository.findById(productId);
    }

    public List<Product> findByUserOrderByProductIDDesc(User user) {
        return productRepository.findByUserOrderByProductIDDesc(user);
    }

}
