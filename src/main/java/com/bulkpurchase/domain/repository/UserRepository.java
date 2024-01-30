package com.bulkpurchase.domain.repository;

import com.bulkpurchase.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);



}