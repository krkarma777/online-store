package com.bulkpurchase.web.controller.users.myPage.info;

import com.bulkpurchase.domain.entity.user.ShippingAddress;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.user.ShippingAddressService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/address")
public class MyPageAddressController {

    private final ShippingAddressService shippingAddressService;
    private final UserAuthValidator userAuthValidator;

    @GetMapping
    public String ShippingAddressList(Model model, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        List<ShippingAddress> shippingAddressList = shippingAddressService.findByUser(user);
        model.addAttribute("shippingAddressList", shippingAddressList);
        return "users/myPage/user_info/address_manage";
    }
}
