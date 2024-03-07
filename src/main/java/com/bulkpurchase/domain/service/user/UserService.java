package com.bulkpurchase.domain.service.user;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.entity.user.VerificationToken;
import com.bulkpurchase.domain.enums.UserStatus;
import com.bulkpurchase.domain.repository.VerificationTokenRepository;
import com.bulkpurchase.domain.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;



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


    public Optional<User> findByRealNameAndEmail(String realname, String email) {
        return userRepository.findByRealNameAndEmail(realname, email);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public boolean existsById(Long userId) {
        return userRepository.existsById(userId);
    }

    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }
}
