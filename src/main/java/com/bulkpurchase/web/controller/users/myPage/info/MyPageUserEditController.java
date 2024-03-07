package com.bulkpurchase.web.controller.users.myPage.info;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile/edit")
public class MyPageUserEditController {

    private final UserAuthValidator userAuthValidator;

    @GetMapping
    public String userEditForm(Model model, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        model.addAttribute("user", user);
        return "users/myPage/user_info/user_edit";
    }
}
