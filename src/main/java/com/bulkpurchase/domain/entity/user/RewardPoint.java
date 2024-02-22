package com.bulkpurchase.domain.entity.user;

import com.bulkpurchase.domain.enums.RewardPointStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reward_points")
@Getter
@Setter
public class RewardPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long RewardPointID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RewardPointStatus status;

    @Column(name = "earned_date", nullable = false)
    private LocalDateTime earnedDate;

    @Column(name = "used_date")
    private LocalDateTime usedDate;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;
}