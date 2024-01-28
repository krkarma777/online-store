package com.bulkpurchase.service;

import com.bulkpurchase.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bulkpurchase.domain.model.User;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

        // 사용자 저장
        return userRepository.save(newUser);
    }

}
