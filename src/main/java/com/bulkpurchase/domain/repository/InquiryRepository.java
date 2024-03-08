package com.bulkpurchase.domain.repository;

import com.bulkpurchase.domain.entity.Inquiry;
import com.bulkpurchase.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findByUser(User user);
}
