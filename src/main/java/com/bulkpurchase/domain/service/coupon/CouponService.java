package com.bulkpurchase.domain.service.coupon;

import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.repository.coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public Optional<Coupon> findById(Long couponID) {
        return couponRepository.findById(couponID);
    }
}
