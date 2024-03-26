package com.bulkpurchase.domain.service.coupon;

import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.repository.coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public Coupon save(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    public List<Coupon> findByUser(User user) {
        return couponRepository.findByCreatedBy(user);
    }
    public Page<Coupon> findByCreatedBy(User user, Pageable pageable) {
        return couponRepository.findByCreatedBy(user, pageable);
    }

    public Optional<Coupon> findById(Long couponID) {
        return couponRepository.findById(couponID);
    }

    public void delete(Coupon coupon) {
        couponRepository.delete(coupon);
    }

    public Coupon findByCode(String couponCode) {
        return couponRepository.findByCode(couponCode);
    }

    public Optional<Coupon> findByIdAndUser(Long couponID, User user) {
        return couponRepository.findByCouponIDAndCreatedBy(couponID, user);
    }

    public List<Coupon> findAll(Sort sort) {
        return couponRepository.findAll(sort);
    }
}
