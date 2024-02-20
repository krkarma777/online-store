package com.bulkpurchase.web.controller.seller;

import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.coupon.CouponService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class CouponManageController {

    private final UserService userService;
    private final CouponService couponService;

    @GetMapping("/coupon/generate")
    public String generateCouponForm(Model model) {
        Coupon coupon = new Coupon();
        model.addAttribute("coupon", coupon);
        return "seller/couponGenerate";
    }

    @PostMapping("/coupon/generate")
    public String generateCoupon(@RequestParam("validFromDate") String validFromDate,
                                 @RequestParam("validFromTime") String validFromTime,
                                 @RequestParam("validUntilDate") String validUntilDate,
                                 @RequestParam("validUntilTime") String validUntilTime, Coupon coupon, Principal principal) {
        LocalDate startDate = LocalDate.parse(validFromDate);
        LocalTime startTime = LocalTime.parse(validFromTime);
        LocalDateTime validFrom = LocalDateTime.of(startDate, startTime);

        LocalDate endDate = LocalDate.parse(validUntilDate);
        LocalTime endTime = LocalTime.parse(validUntilTime);
        LocalDateTime validUntil = LocalDateTime.of(endDate, endTime);

        User user = userService.findByUsername(principal.getName());

        coupon.setValidFrom(validFrom);
        coupon.setValidUntil(validUntil);
        coupon.setCreatedBy(user);
        coupon.setCode(UUID.randomUUID().toString());

        Coupon savedCoupon = couponService.save(coupon);

        return "redirect:/coupon/list";
    }

    @GetMapping("/coupon/list")
    public String couponList(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<Coupon> couponList = couponService.findByUser(user);
        model.addAttribute("couponList", couponList);
        return "/seller/couponList";
    }

    @PostMapping("/coupon/edit")
    public String couponEdit(@RequestParam("validUntilDate") String validUntilDate,
                             @RequestParam("validUntilTime") String validUntilTime,
                             @RequestParam("minimumOrderAmount") Double minimumOrderAmount,
                             @RequestParam("quantity") Integer quantity,
                             @RequestParam("couponID") Long couponID) {
        Coupon coupon = couponService.findById(couponID).orElse(null);
        if (coupon == null) {
            return "error/403";
        }

        LocalDate endDate = LocalDate.parse(validUntilDate);
        LocalTime endTime = LocalTime.parse(validUntilTime);
        LocalDateTime validUntil = LocalDateTime.of(endDate, endTime);

        coupon.setValidUntil(validUntil);
        coupon.setMinimumOrderAmount(minimumOrderAmount);
        coupon.setQuantity(quantity);

        couponService.save(coupon);
        return "redirect:/coupon/list";
    }

    @GetMapping("/coupon/delete/{couponID}")
    public String couponDelete(@PathVariable("couponID") Long couponID, Principal principal) {
        Coupon coupon = couponService.findById(couponID).orElse(null);
        if (coupon == null || principal == null) {
            return "error/403";
        }
        if (!coupon.getCreatedBy().equals(userService.findByUsername(principal.getName()))) {
            return "error/400";
        }
        couponService.delete(coupon);
        return "redirect:/coupon/list";
    }
}
