package com.bulkpurchase.domain.service.user;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        return userOpt.orElse(null);
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
}
