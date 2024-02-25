package com.bulkpurchase.web.controller.users;

import com.bulkpurchase.domain.dto.user.LoginDTO;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.user.CustomUserDetailsService;
import com.bulkpurchase.domain.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@AllArgsConstructor
public class LoginController {

    private final CustomUserDetailsService customUserDetailsService;

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
