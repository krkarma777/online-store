package com.bulkpurchase.domain.repository;

import com.bulkpurchase.domain.entity.user.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByUserUserID(Long userID);

    @Transactional
    @Modifying
    @Query("delete from VerificationToken where token = :token")
    void deleteByToken(@Param("token") String token);

}
