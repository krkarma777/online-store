package com.bulkpurchase.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "Payments")
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentID;

    @ManyToOne
    @JoinColumn(name = "OrderID", nullable = false)
    private Order order;

    @Column
    @Temporal(TemporalType.DATE)
    private Date paymentDate;

    @Column(nullable = false)
    private Double amount;

    @Column(length = 50)
    private String paymentMethod;
}
