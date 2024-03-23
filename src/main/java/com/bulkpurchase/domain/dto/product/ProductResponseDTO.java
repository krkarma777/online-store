package com.bulkpurchase.domain.dto.product;

import com.bulkpurchase.domain.entity.product.Category;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.product.SaveCheck;
import com.bulkpurchase.domain.entity.product.UpdateCheck;
import com.bulkpurchase.domain.entity.user.FavoriteProduct;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.ProductStatus;
import com.bulkpurchase.domain.enums.SalesRegion;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ProductResponseDTO {

    private Long productID;

    private String productName;

    private String description;

    private Double price;

    private Integer stock;

    private List<String> imageUrls;

    private ProductStatus status;

    private Long categoryID;

    private String categoryName;

    public ProductResponseDTO(Product product) {
        this.productID = product.getProductID();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.imageUrls = product.getImageUrls();
        this.status = product.getStatus();
        this.categoryID = product.getCategory().getCategoryID();
        this.categoryName = product.getCategory().getName();
    }
}
