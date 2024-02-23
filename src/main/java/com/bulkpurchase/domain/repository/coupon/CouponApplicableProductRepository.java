package com.bulkpurchase.domain.repository.coupon;

import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.coupon.CouponApplicableProduct;
import com.bulkpurchase.domain.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponApplicableProductRepository extends JpaRepository<CouponApplicableProduct, Long> {


    List<CouponApplicableProduct> findByCoupon(Coupon coupon);

    List<CouponApplicableProduct> findByCouponCouponID(Long couponID);

    Optional<CouponApplicableProduct> findByCouponAndProductId(Coupon coupon, Long productId);

    void deleteByCoupon(Coupon coupon);
}
