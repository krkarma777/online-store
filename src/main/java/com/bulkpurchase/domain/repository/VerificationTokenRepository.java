package com.bulkpurchase.domain.repository;

import com.bulkpurchase.domain.entity.user.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByUserUserID(Long userID);

}
