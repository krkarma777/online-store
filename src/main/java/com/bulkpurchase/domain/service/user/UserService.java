package com.bulkpurchase.domain.service.user;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public Optional<User> findByUserid(Long userId) {
        return userRepository.findById(userId);
    }
}
