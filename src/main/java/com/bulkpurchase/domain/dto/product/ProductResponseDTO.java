package com.bulkpurchase.domain.dto.product;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.enums.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
