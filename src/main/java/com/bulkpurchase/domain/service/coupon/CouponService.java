package com.bulkpurchase.domain.service.coupon;

import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.repository.coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public Coupon save(Coupon coupon) {
        coupon.setCode(UUID.randomUUID().toString());
        return couponRepository.save(coupon);
    }

    public List<Coupon> findByUser(User user) {
        return couponRepository.findByCreatedBy(user);
    }
}
