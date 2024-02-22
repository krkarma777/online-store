package com.bulkpurchase.web.controller.users;

import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.coupon.UserCoupon;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.coupon.CouponApplicableProductService;
import com.bulkpurchase.domain.service.coupon.CouponService;
import com.bulkpurchase.domain.service.coupon.UserCouponService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserCouponController {

    private final CouponService couponService;
    private final CouponApplicableProductService couponApplicableProductService;
    private final UserService userService;
    private final UserCouponService userCouponService;

    @GetMapping("/user/coupon/redeem")
    public String userCouponRedeemForm() {
        return "users/coupon/couponRedeem";
    }

    @PostMapping("/user/coupon/redeem")
    public String userCouponRedeem(Principal principal, @RequestParam("code") String code) {
        if (principal == null) {
            return "error/403";
        }

        User user = userService.findByUsername(principal.getName());
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

        User user = userService.findByUsername(principal.getName());
        List<UserCoupon> userCoupons = userCouponService.findByUser(user);

        model.addAttribute("userCoupons", userCoupons);

        return "users/coupon/userCoupons";

    }
}
