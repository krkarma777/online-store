package com.bulkpurchase.domain.service;

import com.bulkpurchase.domain.entity.OrderDetail;
import com.bulkpurchase.domain.entity.User;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final OrderDetailService orderDetailService;

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

    public void displayPopularProducts() {
        Pageable topTen = PageRequest.of(0, 10); // 상위 10개의 결과만 조회
        List<Object[]> popularProducts = productRepository.findPopularProducts(topTen);
        for (Object[] record : popularProducts) {
            Product product = (Product) record[0];
            Long orderCount = (Long) record[1];

            System.out.println("Product Name: " + product.getProductName() + ", Order Count: " + orderCount);
            // 필요한 추가 작업을 여기에서 수행할 수 있습니다.
        }
    }

    public void delete(Long productID) {
        productRepository.deleteById(productID);
    }

}
