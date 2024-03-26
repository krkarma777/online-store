package com.bulkpurchase.web.controller.seller;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {

    private final UserAuthValidator userAuthValidator;

    @GetMapping("/reviews")
    public String reviewManage(Principal principal, Model model) {
        Long sellerID = userAuthValidator.getCurrentUser(principal).getUserID();
        model.addAttribute("sellerID", sellerID);
        return "/seller/productManage/reviews";
    }

    @GetMapping("/coupon/list")
    public String couponList(Model model, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        model.addAttribute("sellerID", user.getUserID());
        return "seller/couponManage/couponList";
    }

    @GetMapping("/coupon/select/{couponID}")
    public String selectProductForCouponForm(@PathVariable("couponID") Long couponID, Model model) {
        model.addAttribute("couponID", couponID);

        return "seller/couponManage/couponSelectProduct";
    }
}
