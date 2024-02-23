package com.bulkpurchase.domain.dto.product;

import com.bulkpurchase.domain.enums.ProductStatus;
import com.bulkpurchase.domain.enums.SalesRegion;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class PopularProductDTO {

    private Long productID;
    private String productName;
    private String description;
    private Double price;
    private Integer stock;
    private Long userID;
    private List<String> imageUrls;
    private ProductStatus status;
    private Long quantity;

    public PopularProductDTO(Long productID, String productName, String description, Double price, Integer stock, Long userID, ProductStatus status, Long quantity) {
        this.productID = productID;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.userID = userID;
        this.status = status;
        this.quantity = quantity;
    }

    public PopularProductDTO() {
    }
}
