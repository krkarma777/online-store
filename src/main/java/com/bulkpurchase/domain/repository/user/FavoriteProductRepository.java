package com.bulkpurchase.domain.repository.user;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.FavoriteProduct;
import com.bulkpurchase.domain.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Long> {
    List<FavoriteProduct> findByUser(User user);

    boolean existsByUserAndProduct(User user, Product product);

    void deleteByUserAndProduct(User user, Product product);

    FavoriteProduct findByUserAndProduct(User user, Product product);

    Page<FavoriteProduct> findByUser(User user, Pageable page);

    Optional<FavoriteProduct> findByUserAndId(User user, Long id);
}
