package com.bulkpurchase.domain.service.user;

import com.bulkpurchase.config.AppConfig;
import com.bulkpurchase.domain.entity.User;
import com.bulkpurchase.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerProcess(User newUser) {
        System.out.println("userRepository = " + userRepository);
        if (userRepository.existsByUsername(newUser.getUsername())) {
            throw new IllegalStateException("이미 사용중인 사용자명입니다.");
        }

        if (userRepository.existsByEmail(newUser.getEmail())) {
            throw new IllegalStateException("이미 등록된 이메일입니다.");
        }

        // 비밀번호 해시 처리
        String hashedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(hashedPassword);

        // 사용자 저장
        return userRepository.save(newUser);
    }
}
