package com.bulkpurchase.web.controller.seller;

import com.bulkpurchase.domain.entity.Inquiry;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.InquiryService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/seller/inquiries")
@RequiredArgsConstructor
public class SellerInquiryController {

    private final UserAuthValidator userAuthValidator;
    private final InquiryService inquiryService;

    @GetMapping("/{inquiryID}")
    public String inquiry(Model model, Principal principal, @PathVariable("inquiryID") Long inquiryID) {
        User user = userAuthValidator.getCurrentUser(principal);
        Optional<Inquiry> inquiryOpt = inquiryService.findByUserAndInquiryID(user, inquiryID);
        if (inquiryOpt.isPresent()) {
            model.addAttribute("inquiryID", inquiryID);
            return "seller/seller-mypage/customer_center_inquiry";
        } else {
            return "error/403";
        }
    }
    @GetMapping("/list")
    public String inquiries(Model model, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        List<Inquiry> inquiries = inquiryService.findByUser(user);
        model.addAttribute("inquiries", inquiries);
        return "seller/seller-mypage/customer_center_inquiries";
    }
    @GetMapping("/new")
    public String inquiryCreate() {
        return "seller/seller-mypage/customer_center_inquiry_create";
    }
}
