package com.bulkpurchase.web.controller.users;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@AllArgsConstructor
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "/users/login";
    }

    @GetMapping("/login/social")
    public String socialLoginOauth2() {
        return "/users/login";
    }
}
