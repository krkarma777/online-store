package com.bulkpurchase.domain.service.user;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.FavoriteProduct;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.repository.user.FavoriteProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteProductService {

    private final FavoriteProductRepository favoriteProductRepository;

    public List<FavoriteProduct> findAll() {
        return favoriteProductRepository.findAll();
    }

    public void save(FavoriteProduct favoriteProduct) {
        favoriteProductRepository.save(favoriteProduct);
    }

    public Optional<FavoriteProduct> findById(Long id) {
        return favoriteProductRepository.findById(id);
    }

    public Optional<FavoriteProduct> findByUser(User user) {
        return favoriteProductRepository.findByUser(user);
    }

    public boolean toggleFavorite(User user, Product product) {
        if (favoriteProductRepository.existsByUserAndProduct(user, product)) {
            favoriteProductRepository.deleteByUserAndProduct(user, product);
            return false;
        } else {
            FavoriteProduct favoriteProduct = new FavoriteProduct();

            favoriteProduct.setUser(user);
            favoriteProduct.setProduct(product);
            favoriteProductRepository.save(favoriteProduct);
            return true;
        }
    }

    public FavoriteProduct findByUserAndProduct(User user, Product product) {
        return favoriteProductRepository.findByUserAndProduct(user, product);
    }
}
