package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.coupon.*;
import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.coupon.UserCoupon;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.UserRole;
import com.bulkpurchase.domain.service.coupon.CouponService;
import com.bulkpurchase.domain.service.coupon.UserCouponService;
import com.bulkpurchase.domain.validator.coupon.CouponValidatorImpl;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import com.bulkpurchase.web.service.coupon.ApplyCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupon")
public class CouponAPIController {

    private final CouponService couponService;
    private final UserAuthValidator userAuthValidator;
    private final UserCouponService userCouponService;
    private final ApplyCouponService applyCouponService;
    private final CouponValidatorImpl couponValidator;


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
        Coupon coupon = couponOpt.get();
        if (!coupon.getCreatedBy().equals(user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "쿠폰을 삭제할 권한이 없습니다."));
        }
        couponService.delete(coupon);
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
        Sort sort = Sort.by(Sort.Direction.DESC, "couponID");
        List<Coupon> couponList = couponService.findAll(sort);

        List<CouponResponseDTO> dtoList = couponList.stream()
                .map(CouponResponseDTO::new)
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/seller/{sellerID}")
    public ResponseEntity<?> findListBySeller(@PathVariable("sellerID") Long sellerID,
                                              @RequestParam(value = "page",defaultValue = "1") Integer page) {
        User user = userAuthValidator.getCurrentUserByUserID(sellerID);
        if (user.getRole() != UserRole.ROLE_판매자) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "잘못된 요청입니다."));
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "couponID");
        Pageable pageable = PageRequest.of(page - 1, 10, sort);
        Page<Coupon> couponPage = couponService.findByCreatedBy(user, pageable);
        List<CouponResponseDTO> coupons = couponPage.getContent().stream().map(CouponResponseDTO::new).toList();
        return ResponseEntity.ok(Map.of("coupons", coupons, "totalPages", couponPage.getTotalPages(), "currentPage", page));
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


    @GetMapping("/user")
    public ResponseEntity<?> userCoupons(Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        List<UserCoupon> userCoupons = userCouponService.findByUser(user);

        List<UserCouponResponseDTO> dtoList = userCoupons.stream()
                .map(UserCouponResponseDTO::new)
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    @PostMapping("/order/find")
    public ResponseEntity<List<CouponOrderSearchDTO>> findCoupon(@RequestBody CouponRequestDTO couponRequest, Principal principal) {
        Long productID = couponRequest.getProductID();
        Double totalPrice = couponRequest.getTotalPrice();
        User user = userAuthValidator.getCurrentUser(principal);

        List<CouponOrderSearchDTO> couponList = userCouponService.findByUser(user).stream()
                // 스트림으로 변환하고, 쿠폰의 적용 가능한 제품들 중 요청받은 제품 ID와 일치하는 제품이 있는지 확인
                .filter(userCoupon -> userCoupon.getCoupon().getApplicableProducts().stream()
                        .anyMatch(applicableProduct -> applicableProduct.getProductId().equals(productID)))
                // 추가적인 유효성 검사를 통과하는지 확인
                .filter(userCoupon -> couponValidator.isItValidCoupon(user, userCoupon.getCoupon(), productID, totalPrice))
                // 조건을 만족하는 경우, CouponOrderSearchDTO 객체로 매핑
                .map(userCoupon -> new CouponOrderSearchDTO(userCoupon.getCoupon()))
                .toList();

        return ResponseEntity.ok(couponList);
    }


    @PostMapping("/order/apply")
    public ResponseEntity<?> applyCoupon(@RequestParam("productID") Long productID,
                                         @RequestParam("couponID") Long couponID,
                                         @RequestParam("totalPrice") Double totalPrice,
                                         Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        Double applyPrice = applyCouponService.applyCoupon(user, couponID, productID, totalPrice);
        return ResponseEntity.ok(applyPrice);
    }
}
