package com.bulkpurchase.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "Users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Long userID;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "usertype", length = 20)
    private String userType;

    @Column(name = "usergrade", length = 20)
    private String userGrade;

    @Column(name = "createdate")
    @Temporal(TemporalType.DATE)
    private Date createDate;

    @PrePersist
    protected void onCreate() {
        createDate = new Date();
    }
}
