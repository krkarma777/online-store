package com.bulkpurchase.domain.service.user;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void registerProcess(User newUser) {
        if (userRepository.existsByUsername(newUser.getUsername())) {
            throw new IllegalStateException("이미 사용중인 사용자명입니다.");
        }

        if (userRepository.existsByEmail(newUser.getEmail())) {
            throw new IllegalStateException("이미 등록된 이메일입니다.");
        }

        // 비밀번호 해시 처리
        String hashedPassword = bCryptPasswordEncoder.encode(newUser.getPassword());
        newUser.setPassword(hashedPassword);

        // 사용자 저장
        userRepository.save(newUser);
    }
}
