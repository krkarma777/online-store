package com.bulkpurchase.domain.service;

import com.bulkpurchase.domain.entity.Inquiry;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    public Inquiry save(Inquiry inquiry) {
        return inquiryRepository.save(inquiry);
    }

    public void delete(Inquiry inquiry) {
        inquiryRepository.delete(inquiry);
    }

    public Optional<Inquiry> findById(Long inquiryID) {
        return inquiryRepository.findById(inquiryID);
    }

    public List<Inquiry> findByUser(User user) {
        return inquiryRepository.findByUser(user);
    }

    public Optional<Inquiry> findByUserAndInquiryID(User user, Long inquiryID) {
        return inquiryRepository.findByUserAndInquiryID(user, inquiryID);
    }

    public Page<Inquiry> findAll(Pageable pageable) {
        return inquiryRepository.findAll(pageable);
    }
}
