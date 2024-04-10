package com.bulkpurchase.web.controller.seller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seller/inquiries")
@RequiredArgsConstructor
public class SellerInquiryController {

    @GetMapping("/{inquiryID}")
    public String inquiry(Model model, @PathVariable("inquiryID") Long inquiryID) {
        model.addAttribute("inquiryID", inquiryID);
        return "seller/seller-mypage/customer_center_inquiry";
    }
    @GetMapping("/list")
    public String inquiries() {
        return "seller/seller-mypage/customer_center_inquiries";
    }
    @GetMapping("/new")
    public String inquiryCreate() {
        return "seller/seller-mypage/customer_center_inquiry_create";
    }
}
