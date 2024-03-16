package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.coupon.CouponCreateRequestDTO;
import com.bulkpurchase.domain.dto.coupon.CouponResponseDTO;
import com.bulkpurchase.domain.dto.coupon.CouponUpdateRequestDTO;
import com.bulkpurchase.domain.dto.coupon.UserCouponResponseDTO;
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
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupon")
public class CouponAPIController {

    private final CouponService couponService;
    private final UserAuthValidator userAuthValidator;
    private final UserCouponService userCouponService;
    private final CouponApplicableProductService couponApplicableProductService;
    private final ProductService productService;


    @PostMapping
    public ResponseEntity<?> create(@RequestBody CouponCreateRequestDTO couponCreateRequestDTO, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        Coupon coupon = new Coupon(couponCreateRequestDTO);
        coupon.setCreatedBy(user);
        couponService.save(coupon);
        return ResponseEntity.ok(Map.of("message", "쿠폰이 정상적으로 생성되었습니다."));
    }

    @PutMapping("/{couponID}")
    public ResponseEntity<?> update(@PathVariable("couponID") Long couponID, @RequestBody CouponUpdateRequestDTO couponUpdateRequestDTO, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        Optional<Coupon> couponOpt = couponService.findByIdAndUser(couponID, user);
        if (couponOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "쿠폰이 존재하지 않습니다."));
        }
        Coupon coupon = couponOpt.get();
        coupon.setUpdateDTO(couponUpdateRequestDTO);
        couponService.save(coupon);
        return ResponseEntity.ok(Map.of("message", "쿠폰이 정상적으로 수정되었습니다."));
    }

    @DeleteMapping("/{couponID}")
    public ResponseEntity<?> delete(@PathVariable("couponID") Long couponID, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        Optional<Coupon> couponOpt = couponService.findByIdAndUser(couponID, user);
        if (couponOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "쿠폰이 존재하지 않습니다."));
        }
        couponService.delete(couponOpt.get());
        return ResponseEntity.ok(Map.of("message", "쿠폰이 정상적으로 삭제되었습니다."));
    }

    @GetMapping("/{couponID}")
    public ResponseEntity<?> one(@PathVariable("couponID") Long couponID) {
        Optional<Coupon> couponOpt = couponService.findById(couponID);
        if (couponOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "쿠폰이 존재하지 않습니다."));
        }
        CouponResponseDTO couponResponseDTO = new CouponResponseDTO(couponOpt.get());
        return ResponseEntity.ok(couponResponseDTO);
    }

    @GetMapping("/list")
    public ResponseEntity<List<CouponResponseDTO>> listPage() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<Coupon> couponList = couponService.findAll(sort);

        List<CouponResponseDTO> dtoList = couponList.stream()
                .map(CouponResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }


    @PostMapping("/redeem")
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

    @GetMapping("/{couponID}/products")
    public ResponseEntity<?> findApplicableProductsForCoupon(@PathVariable("couponID") Long couponID) {
        List<CouponApplicableProduct> couponApplicableProducts = couponApplicableProductService.findByCouponCouponID(couponID);

        List<ProductForCouponDTO> productForCouponDTOList = couponApplicableProducts.stream()
                .map(CouponApplicableProduct::getProductId)
                .map(productService::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ProductForCouponDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(productForCouponDTOList);
    }

    @GetMapping("/user")
    public ResponseEntity<?> userCoupons(Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        List<UserCoupon> userCoupons = userCouponService.findByUser(user);

        List<UserCouponResponseDTO> dtoList = userCoupons.stream()
                .map(UserCouponResponseDTO::new)
                .toList();

        return ResponseEntity.ok(dtoList);
    }
}
