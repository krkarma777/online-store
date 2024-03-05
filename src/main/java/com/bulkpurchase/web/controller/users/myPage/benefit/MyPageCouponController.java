package com.bulkpurchase.web.controller.users.myPage.benefit;

import com.bulkpurchase.domain.dto.product.ProductForCouponDTO;
import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.coupon.CouponApplicableProduct;
import com.bulkpurchase.domain.entity.coupon.UserCoupon;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.coupon.CouponApplicableProductService;
import com.bulkpurchase.domain.service.coupon.CouponService;
import com.bulkpurchase.domain.service.coupon.UserCouponService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class MyPageCouponController {

    private final CouponService couponService;
    private final CouponApplicableProductService couponApplicableProductService;
    private final UserCouponService userCouponService;
    private final ProductService productService;
    private final UserAuthValidator userAuthValidator;

    @GetMapping
    public String userCoupons(Principal principal, Model model) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. MM. dd. HH:mm");
        model.addAttribute("formatter", formatter);

        User user = userAuthValidator.getCurrentUser(principal);
        List<UserCoupon> userCoupons = userCouponService.findByUser(user);
        model.addAttribute("userCoupons", userCoupons);

        return "users/myPage/benefit/coupons";
    }

    @PostMapping("/redeem")
    @ResponseBody
    public Map<String, Object> userCouponRedeem(Principal principal, @RequestParam("code") String code) {
        Map<String, Object> response = new HashMap<>();
        User user = userAuthValidator.getCurrentUser(principal);
        Coupon coupon = couponService.findByCode(code);

        if (coupon == null) {
            response.put("message", "존재하지 않는 쿠폰입니다.");
        } else if (userCouponService.findByUserAndCoupon(user, coupon).isPresent()) {
            response.put("message", "이미 보유하고 있는 쿠폰입니다.");
        } else {
            UserCoupon userCoupon = new UserCoupon();
            userCoupon.setCoupon(coupon);
            userCoupon.setUser(user);
            userCouponService.save(userCoupon);
            response.put("message", "쿠폰이 성공적으로 등록되었습니다.");
            response.put("redirect", "/coupons"); // 성공 시 리다이렉션할 URL
        }
        return response;
    }




    // 쿠폰에 적용 가능한 상품을 조회하는 컨트롤러 메서드
    @GetMapping("/{couponID}/products")
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
