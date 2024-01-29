package com.bulkpurchase.domain.service;

import com.bulkpurchase.domain.entity.UserEntity;
import com.bulkpurchase.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
