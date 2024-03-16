package com.bulkpurchase.web.controller;

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
@RequestMapping
public class MyPageController {

    private final UserAuthValidator userAuthValidator;

    @GetMapping("/delivery/address/manage")
    public String shippingAddressList() {
        return "users/myPage/user_info/address_manage";
    }

    @GetMapping("/inquiry/{inquiryID}")
    public String inquiryDetail(@PathVariable("inquiryID") Long inquiryID, Model model) {
        model.addAttribute("inquiryID", inquiryID);
        return "users/myPage/active/customer_center_inquiry";
    }

    @GetMapping("/favorites")
    public String favorites() {
        return "users/myPage/active/favorites";
    }

    @GetMapping("/reviews")
    public String myPageReviews() {
        return "users/myPage/active/review/reviews";
    }

    @GetMapping("/coupons")
    public String userCoupons() {
        return "users/myPage/benefit/coupons";
    }

    @GetMapping("/profile/edit")
    public String userEditForm(Model model, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        model.addAttribute("user", user);
        return "users/myPage/user_info/user_edit";
    }

    @GetMapping("/order/list")
    public String orders() {
        return "users/myPage/shopping/orders";
    }

    @GetMapping("/order/detail/{orderID}")
    public String orderDetailInfoForSeller(@PathVariable("orderID") Long orderID, Model model) {
        model.addAttribute("orderID", orderID);
        return "/users/orderDetailForUser";
    }
}
