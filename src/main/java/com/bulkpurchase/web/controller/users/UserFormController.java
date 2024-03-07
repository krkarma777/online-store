package com.bulkpurchase.web.controller.users;

import com.bulkpurchase.domain.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@AllArgsConstructor
public class UserFormController {

    @GetMapping("/login")
    public String login() {
        return "/users/login";
    }

    @GetMapping("/login/social")
    public String socialLoginOauth2() {
        return "/users/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "/users/register";
    }
}
