package com.bulkpurchase.domain.repository.coupon;

import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {


    List<Coupon> findByCreatedBy(User user);
    Page<Coupon> findByCreatedBy(User user, Pageable pageable);

    Coupon findByCode(String couponCode);

    Optional<Coupon> findByCouponIDAndCreatedBy(Long couponID, User user);
}
