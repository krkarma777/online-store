package com.bulkpurchase.domain.repository;

import com.bulkpurchase.domain.entity.user.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}
