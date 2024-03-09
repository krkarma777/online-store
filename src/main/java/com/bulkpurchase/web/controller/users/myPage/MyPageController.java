package com.bulkpurchase.web.controller.users.myPage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class MyPageController {

    @GetMapping("/delivery/address/manage")
    public String shippingAddressList() {
        return "users/myPage/user_info/address_manage";
    }
}
