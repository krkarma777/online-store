package com.bulkpurchase.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "Payments")
@Getter
@Setter
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentID;

    @ManyToOne
    @JoinColumn(name = "OrderID", nullable = false)
    private OrderEntity orderEntity;

    @Column
    @Temporal(TemporalType.DATE)
    private Date paymentDate;

    @Column(nullable = false)
    private Double amount;

    @Column(length = 50)
    private String paymentMethod;
}
