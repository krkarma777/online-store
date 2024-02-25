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

    @PostMapping("/login")
    public String loginAccess(HttpServletRequest request, HttpServletResponse response) {
        String authorization = request.getHeader("Authorization");
        System.out.println("goToHome request authorization = " + authorization);
        String authorization2 = response.getHeader("Authorization");
        System.out.println("goToHome response authorization = " + authorization2);
        return "/home";
    }
}
