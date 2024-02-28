package com.bulkpurchase.web.controller.users;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.VerificationTokenService;
import com.bulkpurchase.domain.service.user.UserService;
import com.bulkpurchase.web.service.verification.EmailService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/verify")
public class UserVerificationController {

    private final UserService userService;
    private final UserAuthValidator userAuthValidator;
    private final EmailService emailService;
    private final VerificationTokenService verificationTokenService;

    @GetMapping("/result")
    public String verifyUser(@RequestParam("token") String token) {
        if (emailService.verifyToken(token)) {
            verificationTokenService.deleteByToken(token);
            return "verifyByEmail/verify_success";
        } else {
            return "verifyByEmail/verify_fail";
        }
    }

    @GetMapping
    public String verifyForm(@RequestParam("username") String username, Model model) {
        User user = userAuthValidator.getCurrentUserByUsername(username);
        model.addAttribute("user", user);
        return "verifyByEmail/verification_email";
    }

    @PostMapping("/send")
    public String emailSend(@RequestParam("username") String username, @RequestParam("email") String email) throws MessagingException {
        User user = userAuthValidator.getCurrentUserByUsername(username);
        String userEmailOrigin = user.getEmail();
        if (!userEmailOrigin.equals(email)) {
            user.setEmail(email);
            userService.save(user);
        }
        emailService.sendVerificationEmail(user);

        return "redirect:/main"; // 이메일 전송 후 리다이렉트할 페이지
    }
}
