package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.coupon.CouponApplicableProductRequestDTO;
import com.bulkpurchase.domain.dto.product.ProductForCouponDTO;
import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.coupon.CouponApplicableProduct;
import com.bulkpurchase.domain.service.coupon.CouponApplicableProductService;
import com.bulkpurchase.domain.service.coupon.CouponService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.validator.CouponApplicableProductValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupon-applicable-product")
public class CouponApplicableProductAPIController {

    private final CouponApplicableProductService couponApplicableProductService;
    private final CouponApplicableProductValidator validator;
    private final ProductService productService;
    private final CouponService couponService;

    @GetMapping("/{couponID}")
    public ResponseEntity<?> findApplicableProductsForCoupon(@PathVariable("couponID") Long couponID) {
        List<CouponApplicableProduct> couponApplicableProducts = couponApplicableProductService.findByCouponCouponID(couponID);

        List<ProductForCouponDTO> productForCouponDTOS = couponApplicableProducts.stream()
                .map(CouponApplicableProduct::getProductId)
                .map(productService::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ProductForCouponDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(productForCouponDTOS);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> selectProductForCoupon(@RequestBody CouponApplicableProductRequestDTO requestDTO,
                                                    Principal principal) {

        Coupon coupon = couponService.findById(requestDTO.getCouponID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다."));

        validator.validateCouponOwner(principal, coupon);

        List<Long> productIDs = requestDTO.getProductIDs();
        if (productIDs != null && !productIDs.isEmpty()) {
            validator.validateAndApplyProductsToCoupon(productIDs, coupon, principal);
        } else {
            couponApplicableProductService.deleteByCoupon(coupon);
        }

        return ResponseEntity.ok(Map.of("message", "쿠폰에 상품(들)이 정상적으로 적용되었습니다."));
    }
}
