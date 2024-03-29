package com.bulkpurchase.domain.service.coupon;

import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.coupon.CouponApplicableProduct;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.repository.coupon.CouponApplicableProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponApplicableProductService {

    private final CouponApplicableProductRepository couponApplicableProductRepository;

    public void save(CouponApplicableProduct couponApplicableProduct) {
        couponApplicableProductRepository.save(couponApplicableProduct);
    }

    public List<CouponApplicableProduct> findByCoupon(Coupon coupon) {
        return couponApplicableProductRepository.findByCoupon(coupon);
    }

    public void delete(CouponApplicableProduct couponApplicableProduct) {
        couponApplicableProductRepository.delete(couponApplicableProduct);
    }

    public List<CouponApplicableProduct> findByCouponCouponID(Long couponID) {
        return couponApplicableProductRepository.findByCouponCouponID(couponID);
    }

    public Optional<CouponApplicableProduct> findByCouponAndProductId(Coupon coupon, Long productID) {
        return couponApplicableProductRepository.findByCouponAndProductId(coupon, productID);
    }

    public void deleteByCoupon(Coupon coupon) {
        couponApplicableProductRepository.deleteByCoupon(coupon);
    }
}
