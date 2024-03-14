package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.coupon.CouponCreateRequestDTO;
import com.bulkpurchase.domain.dto.coupon.CouponUpdateRequestDTO;
import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.coupon.CouponService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupon")
public class CouponController {

    private final CouponService couponService;
    private final UserAuthValidator userAuthValidator;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CouponCreateRequestDTO couponCreateRequestDTO) {
        Coupon coupon = new Coupon(couponCreateRequestDTO);
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
        return ResponseEntity.ok(couponOpt.get());
    }

    @GetMapping("/list")
    public ResponseEntity<?> listPage() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<Coupon> couponList = couponService.findAll(sort);
        return ResponseEntity.ok(couponList);
    }
}
