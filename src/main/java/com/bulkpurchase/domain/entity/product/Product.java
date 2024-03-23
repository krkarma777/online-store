package com.bulkpurchase.domain.entity.product;


import com.bulkpurchase.domain.dto.product.CreateProductDTO;
import com.bulkpurchase.domain.entity.user.FavoriteProduct;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.ProductStatus;
import com.bulkpurchase.domain.enums.SalesRegion;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Products")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups =  UpdateCheck.class)
    private Long productID;

    @NotEmpty(groups = {SaveCheck.class, UpdateCheck.class})
    @Column(nullable = false, length = 100)
    private String productName;

    @Column
    @NotEmpty(groups = {SaveCheck.class, UpdateCheck.class})
    @Lob
    private String description;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Column(nullable = false)
    @Range(min = 1000, max = 1000000, groups = {SaveCheck.class})
    private Double price;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Column(nullable = false)
    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID")
    @JsonBackReference
    private User user;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_sales_regions", joinColumns = @JoinColumn(name = "productID"))
    @Column(name = "region", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<SalesRegion> salesRegions = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "product_image_urls", joinColumns = @JoinColumn(name = "productID"))
    @Column(name = "imageUrl")
    private List<String> imageUrls;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20, columnDefinition = "varchar(20) default 'ACTIVE'")
    private ProductStatus status = ProductStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryID")
    private Category category;


    @OneToMany(mappedBy = "product")
    private List<FavoriteProduct> favoritedByUsers;

    public ProductStatus getOppositeStatus() {
        return this.status == ProductStatus.ACTIVE ? ProductStatus.INACTIVE : ProductStatus.ACTIVE;
    }

    public Product(CreateProductDTO createProductDTO, User user) {
        this.productName = createProductDTO.getProductName();
        this.description = createProductDTO.getDescription();
        this.price = createProductDTO.getPrice();
        this.stock = createProductDTO.getStock();
        this.user = user;
        this.imageUrls = createProductDTO.getImageUrls();
        this.category = new Category(createProductDTO.getCategoryID());
    }

    public Product() {
    }
}

