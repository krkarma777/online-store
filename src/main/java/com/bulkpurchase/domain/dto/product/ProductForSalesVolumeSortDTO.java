package com.bulkpurchase.domain.dto.product;

import com.bulkpurchase.domain.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductForSalesVolumeSortDTO {
    private Long productID;
    private String productName;
    private Double price;
    private Integer stock;
    private String username; // 판매자 이름
    private List<String> imageUrls;
    private Long totalQuantity;
    private User user;

    public ProductForSalesVolumeSortDTO(Long productID, String productName, Double price, Integer stock, String username, List<String> imageUrls, Long totalQuantity, User user) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.username = username;
        this.imageUrls = imageUrls;
        this.totalQuantity = totalQuantity;
        this.user = user;
    }

    public ProductForSalesVolumeSortDTO(Long productID, String productName, Double price, Integer stock, String username, Long totalQuantity, User user) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.username = username;
        this.totalQuantity = totalQuantity;
        this.user = user;
    }



    public ProductForSalesVolumeSortDTO() {
    }
}
