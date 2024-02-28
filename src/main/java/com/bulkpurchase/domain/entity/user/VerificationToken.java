package com.bulkpurchase.domain.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "Verification_Token")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long VerificationTokenID;
    private String token;
    private LocalDateTime expiryDate;

    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }

    @Override
    public String toString() {
        return "VerificationToken{" +
                "VerificationTokenID=" + VerificationTokenID +
                ", token='" + token + '\'' +
                ", expiryDate=" + expiryDate +
                ", user=" + user +
                '}';
    }
}