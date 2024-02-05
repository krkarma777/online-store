package com.bulkpurchase.domain.entity;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.enums.SalesRegion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long userID;

    @Column(nullable = false, length = 50, unique = true)
    private String username;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(length = 20)
    private String role;

    @Column(length = 20)
    private String userGrade;

    @PrePersist
    protected void onCreate() {
        createDate = new Date();
    }

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_sales_regions", joinColumns = @JoinColumn(name = "userid"))
    @Column(name = "region")
    @Enumerated(EnumType.STRING)
    private Set<SalesRegion> salesRegions = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Product> products = new HashSet<>();
}
