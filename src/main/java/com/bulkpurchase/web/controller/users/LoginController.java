package com.bulkpurchase.web.controller.users;

import com.bulkpurchase.domain.dto.user.LoginDTO;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.user.CustomUserDetailsService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@AllArgsConstructor
public class LoginController {

    private static final String LOGIN_ERROR_FLAG = "loginError";
    private static final String ERROR_MESSAGE = "errorMessage";

    private final CustomUserDetailsService customUserDetailsService;

    private final UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exceptionMessage,
                        Model model) {
        if (error != null) {
            model.addAttribute(LOGIN_ERROR_FLAG, true);
            model.addAttribute(ERROR_MESSAGE, exceptionMessage);
        }
        return "/users/login";
    }

    @PostMapping("/loginProc")
    public String loginProc(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "exception", required = false) String exceptionMessage,
                            LoginDTO loginDTO,
                            Model model) {

        customUserDetailsService.loadUserByUsername(loginDTO.getUsername());
        return "redirect:/";
    }
}
