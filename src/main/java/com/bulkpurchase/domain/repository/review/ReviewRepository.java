package com.bulkpurchase.domain.repository.review;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.review.Review;
import com.bulkpurchase.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findByUserAndProduct(User user, Product product);
}
