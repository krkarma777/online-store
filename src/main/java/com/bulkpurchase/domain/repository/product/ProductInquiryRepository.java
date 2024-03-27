package com.bulkpurchase.domain.repository.product;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.product.ProductInquiry;
import com.bulkpurchase.domain.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductInquiryRepository extends JpaRepository<ProductInquiry, Long> {

    List<ProductInquiry> findByProductProductID(Long productID);

    List<ProductInquiry> findByProductUser(User user);

    Page<ProductInquiry> findByProductUser(User user, Pageable pageable);

    Page<ProductInquiry> findByProduct(Product product, Pageable pageable);
}
