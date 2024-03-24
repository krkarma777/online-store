package com.bulkpurchase.web.controller.seller;

import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class SellerController {

    private final UserAuthValidator userAuthValidator;

    @GetMapping("/seller/reviews")
    public String reviewManage(Principal principal, Model model) {
        Long sellerID = userAuthValidator.getCurrentUser(principal).getUserID();
        model.addAttribute("sellerID", sellerID);
        return "/seller/productManage/reviews";
    }
}
