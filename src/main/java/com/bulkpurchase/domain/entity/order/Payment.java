package com.bulkpurchase.domain.entity.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "Payments")
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OrderID", nullable = false)
    private Order order;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate = new Date();

    @Column(nullable = false)
    private Double amount;

    @Column(length = 50)
    private String paymentMethod;

    // 결제 상태 필드 추가
    @Column(length = 20, nullable = false)
    private String status; // 예: "APPROVED", "CANCELLED", "PENDING", etc.

    public Payment(Order order, Double amount, String paymentMethod) {
        this.order = order;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public Payment() {
    }
}
