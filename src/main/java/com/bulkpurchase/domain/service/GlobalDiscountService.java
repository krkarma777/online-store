package com.bulkpurchase.domain.service;

import com.bulkpurchase.domain.entity.discount.GlobalDiscount;
import com.bulkpurchase.domain.repository.GlobalDiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GlobalDiscountService {

    private final GlobalDiscountRepository globalDiscountRepository;

    public void save(GlobalDiscount globalDiscount) {
        globalDiscountRepository.save(globalDiscount);
    }

    public List<GlobalDiscount> findAll() {
        return globalDiscountRepository.findAll(Sort.by(Sort.Direction.ASC, "globalDiscountID"));
    }

    public Optional<GlobalDiscount> findById(Long discountId) {
        return globalDiscountRepository.findById(discountId);
    }
}
