package com.bulkpurchase.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "Orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderID;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @Column
    @Temporal(TemporalType.DATE)
    private Date orderDate;

    @Column(nullable = false)
    private Double totalPrice;
}
