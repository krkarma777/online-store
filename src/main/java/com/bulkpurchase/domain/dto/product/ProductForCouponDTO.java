package com.bulkpurchase.domain.dto.product;

import com.bulkpurchase.domain.entity.product.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductForCouponDTO {
    private Long productID;
    private String productName;
    private String category;

    public ProductForCouponDTO(Product product) {
        this.productID = product.getProductID();
        this.productName = product.getProductName();
        this.category = product.getCategory().getName();
    }

    public ProductForCouponDTO() {
    }

    @Override
    public String toString() {
        return "ProductForCouponDTO{" +
                "productID=" + productID +
                ", productName='" + productName + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
