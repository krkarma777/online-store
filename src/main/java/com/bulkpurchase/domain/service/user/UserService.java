package com.bulkpurchase.domain.service.user;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.entity.user.VerificationToken;
import com.bulkpurchase.domain.repository.VerificationTokenRepository;
import com.bulkpurchase.domain.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private VerificationTokenRepository verificationTokenRepository;


    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public Optional<User> findByUsernameOpt(String username) {
        return userRepository.findByUsername(username);
    }


    public void save(User user) {
        userRepository.save(user);
    }

    public Optional<User> findByUserid(Long userId) {
        return userRepository.findById(userId);
    }

    public boolean existsByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean existsByEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAllOrderByUserID() {
        return userRepository.findAllOrderByUserID();
    }

    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken();
        myToken.setUser(user);
        myToken.setToken(token);
        myToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // 24시간 후 만료
        verificationTokenRepository.save(myToken);
    }

    public boolean verifyToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null || verificationToken.isExpired()) {
            return false;
        }
        User user = verificationToken.getUser();
        // 사용자 인증 로직 (예: 사용자 상태 업데이트)
        return true;
    }
}
