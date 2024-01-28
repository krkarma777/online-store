package com.bulkpurchase.web.service;

import com.bulkpurchase.domain.repository.UserRepository;
import com.bulkpurchase.domain.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserRegisterService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(User newUser) {
        // 이메일 중복 확인
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new IllegalStateException("이미 등록된 이메일입니다.");
        }

        // 사용자명 중복 확인 (필요한 경우)
        if (userRepository.findByUsername(newUser.getUsername()).isPresent()) {
            throw new IllegalStateException("이미 사용중인 사용자명입니다.");
        }

        // 비밀번호 해시 처리
        String hashedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(hashedPassword);

        // 사용자 저장
        return userRepository.save(newUser);
    }
}
