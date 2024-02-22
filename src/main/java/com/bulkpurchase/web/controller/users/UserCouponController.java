package com.bulkpurchase.web.controller.users;

import com.bulkpurchase.domain.dto.ProductForCouponDTO;
import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.coupon.CouponApplicableProduct;
import com.bulkpurchase.domain.entity.coupon.UserCoupon;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.coupon.CouponApplicableProductService;
import com.bulkpurchase.domain.service.coupon.CouponService;
import com.bulkpurchase.domain.service.coupon.UserCouponService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class UserCouponController {

    private final CouponService couponService;
    private final CouponApplicableProductService couponApplicableProductService;
    private final UserService userService;
    private final UserCouponService userCouponService;
    private final ProductService productService;

    @GetMapping("/user/coupon/redeem")
    public String userCouponRedeemForm() {
        return "users/coupon/couponRedeem";
    }

    @PostMapping("/user/coupon/redeem")
    public String userCouponRedeem(Principal principal, @RequestParam("code") String code) {
        if (principal == null) {
            return "error/403";
        }

        User user = userService.findByUsername(principal.getName()).orElse(null);
        Coupon coupon = couponService.findByCode(code);

        if (coupon == null) {
            return "error/403";
        }

        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setCoupon(coupon);
        userCoupon.setUser(user);
        userCouponService.save(userCoupon);

        return "redirect:/user/coupon/list";
    }

    @GetMapping("/user/coupon/list")
    public String userCoupons(Principal principal, Model model) {

        User user = userService.findByUsername(principal.getName()).orElse(null);
        List<UserCoupon> userCoupons = userCouponService.findByUser(user);

        model.addAttribute("userCoupons", userCoupons);

        return "users/coupon/userCoupons";

    }

    // 쿠폰에 적용 가능한 상품을 조회하는 컨트롤러 메서드
    @GetMapping("/user/coupons/{couponID}/products")
    public ResponseEntity<?> findApplicableProductsForCoupon(@PathVariable("couponID") Long couponID) {
        List<CouponApplicableProduct> couponApplicableProducts = couponApplicableProductService.findByCouponCouponID(couponID);

        // Stream API를 사용하여 변환 과정을 더 간결하게 처리
        List<ProductForCouponDTO> productForCouponDTOList = couponApplicableProducts.stream()
                .map(CouponApplicableProduct::getProductId)
                .map(productService::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ProductForCouponDTO::new)
                .collect(Collectors.toList());

        // 상품 정보를 담은 DTO 리스트를 JSON 형태로 반환
        return ResponseEntity.ok(productForCouponDTOList);
    }
}
