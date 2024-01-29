package com.bulkpurchase.domain.entity;

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
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Long userID;

    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "role", length = 20)
    private String role;

    @Column(name = "usergrade", length = 20)
    private String userGrade;

    @Column(name = "createdate")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    @PrePersist
    protected void onCreate() {
        createDate = new Date();
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_sales_regions", joinColumns = @JoinColumn(name = "userid"))
    @Column(name = "region")
    @Enumerated(EnumType.STRING)
    private Set<SalesRegion> salesRegions = new HashSet<>();

    @Override
    public String toString() {
        return "UserEntity{" +
                "userID=" + userID +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", userGrade='" + userGrade + '\'' +
                ", createDate=" + createDate +
                ", salesRegions=" + salesRegions +
                '}';
    }
}
