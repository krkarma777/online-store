package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.product.ProductForCouponDTO;
import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.coupon.CouponApplicableProduct;
import com.bulkpurchase.domain.service.coupon.CouponApplicableProductService;
import com.bulkpurchase.domain.service.coupon.CouponService;
import com.bulkpurchase.domain.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupon-applicable-product")
public class CouponApplicableProductAPIController {

    private final CouponApplicableProductService couponApplicableProductService;
    private final ProductService productService;
    private final CouponService couponService;

    @PostMapping("/applicableProduct")
    @Transactional
    public String applicableProduct(@RequestParam("couponID") Long couponID,
                                    @RequestParam(value = "productIDs", required = false) List<Long> productIDs) {
        Coupon coupon = couponService.findById(couponID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));

        couponApplicableProductService.deleteByCoupon(coupon);

        if (productIDs != null) {
            productIDs.forEach(productID -> {
                CouponApplicableProduct cap = new CouponApplicableProduct(coupon, productID);
                couponApplicableProductService.save(cap);
            });
        }

        return "redirect:/seller/coupon/list";
    }

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
}
