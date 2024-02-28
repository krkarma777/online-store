package com.bulkpurchase.domain.service;

import com.bulkpurchase.domain.entity.user.VerificationToken;
import com.bulkpurchase.domain.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationToken save(VerificationToken verificationToken) {
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    public Optional<VerificationToken> findByUserUserID(Long userID) {
        return verificationTokenRepository.findByUserUserID(userID);
    }

    public Optional<VerificationToken> findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    public void delete(VerificationToken verificationToken) {
        System.out.println("verificationToken = " + verificationToken.toString());
        verificationTokenRepository.delete(verificationToken);
    }

    public List<VerificationToken> findAll() {
        return verificationTokenRepository.findAll();
    }

    public void deleteExpiredTokens() {
        List<VerificationToken> tokens = findAll();

        tokens.stream()
                .filter(VerificationToken::isExpired)
                .forEach(this::delete);
    }
}
