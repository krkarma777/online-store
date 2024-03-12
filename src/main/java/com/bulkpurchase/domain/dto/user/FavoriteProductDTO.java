package com.bulkpurchase.domain.dto.user;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.FavoriteProduct;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FavoriteProductDTO {

    private Long id;

    private LocalDateTime favoritedAt;

    private Product product;

    public FavoriteProductDTO(FavoriteProduct favoriteProduct) {
        this.id = favoriteProduct.getId();
        this.favoritedAt = favoriteProduct.getFavoritedAt();
        this.product = favoriteProduct.getProduct();
    }

    public FavoriteProductDTO() {
    }
}
