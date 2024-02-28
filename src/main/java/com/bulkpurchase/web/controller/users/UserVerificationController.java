package com.bulkpurchase.web.controller.users;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.user.UserService;
import com.bulkpurchase.web.service.EmailService;
import com.bulkpurchase.web.validator.user.UserAuthValidator;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/verify")
public class UserVerificationController {

    private final UserService userService;
    private final UserAuthValidator userAuthValidator;
    private final EmailService emailService;

    @GetMapping("/result")
    public String verifyUser(@RequestParam("token") String token) {
        if (userService.verifyToken(token)) {
            return "verifyByEmail/verify_success";
        } else {
            return "verifyByEmail/verify_fail";
        }
    }

    @GetMapping
    public String verifyForm(Model model, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        model.addAttribute("username", user.getRealName());

        return "verifyByEmail/verification_email";
    }

    @GetMapping("/send")
    public String emailSend(Principal principal) throws MessagingException {
        User user = userAuthValidator.getCurrentUser(principal);
        String verificationUrl = "test";
        emailService.sendVerificationEmail("yjoa777@naver.com", user.getRealName(), verificationUrl);

        return "redirect:/verify"; // 이메일 전송 후 리다이렉트할 페이지
    }
}
