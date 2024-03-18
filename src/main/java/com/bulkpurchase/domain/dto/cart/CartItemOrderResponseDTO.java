package com.bulkpurchase.domain.dto.cart;

import com.bulkpurchase.domain.entity.cart.CartItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemOrderResponseDTO {

    private Long productID;
    private String productName;
    private Double price;
    private int quantity;

    public CartItemOrderResponseDTO(CartItem cartItem) {
        this.productID = cartItem.getProduct().getProductID();
        this.productName = cartItem.getProduct().getProductName();
        this.price = cartItem.getProduct().getPrice();
        this.quantity = cartItem.getQuantity();
    }

    public CartItemOrderResponseDTO(Long productID, String productName, Double price, int quantity) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public CartItemOrderResponseDTO() {
    }
}
