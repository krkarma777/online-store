package com.bulkpurchase.domain.service;

import com.bulkpurchase.domain.entity.user.VerificationToken;
import com.bulkpurchase.domain.repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Transactional
    public void delete(VerificationToken verificationToken) {
        verificationTokenRepository.delete(verificationToken);
    }

    public List<VerificationToken> findAll() {
        return verificationTokenRepository.findAll();
    }

    @Transactional
    public void deleteByToken(String token) {
        verificationTokenRepository.deleteByToken(token);
    }
}
