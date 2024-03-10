package com.bulkpurchase.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class MyPageController {

    @GetMapping("/delivery/address/manage")
    public String shippingAddressList() {
        return "users/myPage/user_info/address_manage";
    }

    @GetMapping("/inquiry/{inquiryID}")
    public String inquiryDetail(@PathVariable("inquiryID") Long inquiryID, Model model) {
        model.addAttribute("inquiryID", inquiryID);
        return "users/myPage/active/customer_center_inquiry";
    }
}
