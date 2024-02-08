package com.bulkpurchase.domain.entity.user;

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
    private String realName; // 실제 이름 필드 추가

    @Column(nullable = false, length = 100)
    private String password;

    @Column(length = 20)
    private String role;

    @Column(length = 20)
    private String userGrade = "1"; // 기본값을 "1"로 설정

    @Column(nullable = false, length = 20)
    private String phoneNumber; // 휴대폰 번호 필드 추가

    @Column(length = 255)
    private String address; // 주소 필드 추가

    @Column(length = 255) // detailAddress 필드 추가
    private String detailAddress; // 세부 주소 필드

    @Column(length = 20) // 우편번호 필드 추가
    private String zipCode; // 우편번호

    @PrePersist
    protected void onCreate() {
        createDate = new Date();
        if (userGrade == null) { // userGrade가 null일 경우 기본값으로 "1" 설정
            userGrade = "1";
        }
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
