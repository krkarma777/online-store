package com.bulkpurchase.web.controller.users;

import com.bulkpurchase.web.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@AllArgsConstructor
public class LoginController {

    UserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "/users/login";
    }
}