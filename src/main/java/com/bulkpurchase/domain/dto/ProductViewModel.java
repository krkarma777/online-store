package com.bulkpurchase.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductViewModel {
    private Long productID;
    private String name;
    private Double price;
    private String description;
    private String imageUrl; // 상품 이미지 URL 필드 추가

    public ProductViewModel(Long productID, String name, Double price, String description, String imageUrl) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl; // 생성자에 imageUrl 초기화 추가
    }

    public ProductViewModel() {
    }
}