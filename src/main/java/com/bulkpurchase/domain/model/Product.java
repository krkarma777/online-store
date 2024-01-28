package com.bulkpurchase.domain.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productID;

    @Column(nullable = false, length = 100)
    private String productName;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column
    private Integer stock;

    @Column(length = 200)
    private String imageURL;
}

