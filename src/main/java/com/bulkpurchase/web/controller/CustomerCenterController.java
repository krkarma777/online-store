package com.bulkpurchase.web.controller;

import com.bulkpurchase.domain.entity.Inquiry;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.InquiryService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class CustomerCenterController {

    private final InquiryService inquiryService;
    private final UserAuthValidator userAuthValidator;

    @GetMapping("/inquiries")
    public String inquiriesForm(Model model, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        List<Inquiry> inquiries = inquiryService.findByUser(user);
        model.addAttribute("inquiries", inquiries);

        return "users/myPage/active/customer_center_inquiries";
    }
}
