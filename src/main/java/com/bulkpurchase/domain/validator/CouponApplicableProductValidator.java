package com.bulkpurchase.domain.validator;

import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.coupon.CouponApplicableProduct;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.coupon.CouponApplicableProductService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CouponApplicableProductValidator {

    private final UserAuthValidator userAuthValidator;
    private final CouponApplicableProductService couponApplicableProductService;
    private final ProductService productService;

    public void validateCouponOwner(Principal principal, Coupon coupon) {
        User user = userAuthValidator.getCurrentUser(principal);
        if (!user.equals(coupon.getCreatedBy())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "권한이 없습니다.");
        }
    }

    public void validateAndApplyProductsToCoupon(List<Long> productIDs, Coupon coupon) {
        List<Product> products = productIDs.stream()
                .map(this::validateAndGetProduct)
                .toList();

        couponApplicableProductService.deleteByCoupon(coupon);
        products.forEach(product -> couponApplicableProductService.save(new CouponApplicableProduct(coupon, product.getProductID())));
    }

    private Product validateAndGetProduct(Long productID) {
        return productService.findById(productID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "잘못된 상품 정보입니다."));
    }
}
