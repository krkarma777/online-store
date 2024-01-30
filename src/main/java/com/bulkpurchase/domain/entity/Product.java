package com.bulkpurchase.domain.entity;


import com.bulkpurchase.domain.enums.SalesRegion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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
    @NotNull
    private Long productID;

    @NotEmpty
    @Column(nullable = false, length = 100)
    private String productName;

    @NotEmpty
    @Column(length = 500)
    private String description;

    @NotNull
    @Column(nullable = false)
    @Range(min = 1000, max = 1000000)
    private Long price;

    @NotNull
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

    @Override
    public String toString() {
        return "Product{" +
                "productID=" + productID +
                ", productName='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", imageURL='" + imageURL + '\'' +
                ", user=" + user +
                ", salesRegions=" + salesRegions +
                '}';
    }
}

