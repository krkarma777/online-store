package com.bulkpurchase.web.controller.users;

import com.bulkpurchase.domain.service.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@AllArgsConstructor
public class LoginController {

    private static final String LOGIN_ERROR_FLAG = "loginError";
    private static final String ERROR_MESSAGE = "errorMessage";

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
}
