package com.bulkpurchase.domain.repository.coupon;

import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {


    List<Coupon> findByCreatedBy(User user);

    Coupon findByCode(String couponCode);
}
