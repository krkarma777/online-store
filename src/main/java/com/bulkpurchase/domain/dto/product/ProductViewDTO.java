package com.bulkpurchase.domain.dto.product;

import com.bulkpurchase.domain.entity.product.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductViewDTO {
    private Long productID;
    private String name;
    private Double price;
    private String description;
    private String imageUrl; // 상품 이미지 URL 필드 추가

    public ProductViewDTO(Long productID, String name, Double price, String description, String imageUrl) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl; // 생성자에 imageUrl 초기화 추가
    }
    public ProductViewDTO(Product product) {
        this.productID = product.getProductID();
        this.name = product.getProductName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.imageUrl = product.getImageUrls().get(0); // 생성자에 imageUrl 초기화 추가
    }

    public ProductViewDTO() {
    }


}