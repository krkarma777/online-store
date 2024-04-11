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
@RequestMapping("/seller")
public class SellerController {

    private final UserAuthValidator userAuthValidator;

    @GetMapping
    public String sellerPageForm() {
        return "seller/sellerPage";
    }

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

    @GetMapping("/sales")
    public String salesView() {
        return "/seller/sales";
    }

    @GetMapping("/inquiries")
    public String inquiriesManage(Principal principal, Model model) {
        User user = userAuthValidator.getCurrentUser(principal);
        model.addAttribute("sellerID", user.getUserID());
        return "/seller/productManage/inquiries";
    }

    @GetMapping("/orders")
    public String manageOrders() {
        return "seller/orderManage/orders";
    }

    @GetMapping("/orders/detail/{orderDetailID}")
    public String orderDetailInfoForSeller(@PathVariable("orderDetailID") Long orderDetailID, Model model) {
        model.addAttribute("orderDetailID", orderDetailID);
        return "seller/orderManage/orderDetailForSeller";
    }

    @GetMapping("/inquiries/{inquiryID}")
    public String inquiry(Model model, @PathVariable("inquiryID") Long inquiryID) {
        model.addAttribute("inquiryID", inquiryID);
        return "seller/seller-mypage/customer_center_inquiry";
    }
    @GetMapping("/inquiries/list")
    public String inquiries() {
        return "seller/seller-mypage/customer_center_inquiries";
    }
    @GetMapping("/inquiries/new")
    public String inquiryCreate() {
        return "seller/seller-mypage/customer_center_inquiry_create";
    }

    @GetMapping("/seller/profile/edit")
    public String sellerProfile(Principal principal, Model model) {
        User user = userAuthValidator.getCurrentUser(principal);
        model.addAttribute("user", user);
        return "seller/seller-mypage/user_edit";
    }
}
