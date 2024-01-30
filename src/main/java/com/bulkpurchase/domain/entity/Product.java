package com.bulkpurchase.domain.entity;


import com.bulkpurchase.domain.enums.SalesRegion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Products")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productID;

    @NotNull
    @Column(nullable = false, length = 100)
    private String productName;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    @Range(min = 1000, max = 1000000)
    private Long price;

    @Column(nullable = false)
    private Integer stock;

    @Column(length = 200)
    private String imageURL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID")
    private User user;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_sales_regions", joinColumns = @JoinColumn(name = "productID"))
    @Column(name = "region", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<SalesRegion> salesRegions = new HashSet<>();
}

