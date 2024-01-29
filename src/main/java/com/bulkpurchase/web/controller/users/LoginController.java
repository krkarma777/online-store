package com.bulkpurchase.web.controller.users;

import com.bulkpurchase.domain.entity.UserEntity;
import com.bulkpurchase.domain.service.LoginService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@AllArgsConstructor
public class LoginController {

    LoginService loginService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "/users/login";
    }
}