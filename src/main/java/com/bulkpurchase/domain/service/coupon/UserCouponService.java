package com.bulkpurchase.domain.service.coupon;

import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.coupon.UserCoupon;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.repository.coupon.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCouponService {

    private final UserCouponRepository userCouponRepository;

    public void save(UserCoupon userCoupon) {
        userCouponRepository.save(userCoupon);
    }
    public void delete(UserCoupon userCoupon) {
        userCouponRepository.delete(userCoupon);
    }
    public Optional<UserCoupon> findById(Long userCouponID) {
        return userCouponRepository.findById(userCouponID);
    }

    public List<UserCoupon> findByUser(User user) {
        return userCouponRepository.findByUser(user);
    }

    public List<UserCoupon> findAll() {
        return userCouponRepository.findAll();
    }

    public Optional<UserCoupon> findByUserAndCoupon(User user, Coupon coupon) {
        return userCouponRepository.findByUserAndCoupon(user, coupon);
    }
}
