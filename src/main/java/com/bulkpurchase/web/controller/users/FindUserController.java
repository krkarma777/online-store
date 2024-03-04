package com.bulkpurchase.web.controller.users;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.user.UserService;
import com.bulkpurchase.web.service.verification.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FindUserController {

    private final UserService userService;
    private final EmailService emailService;

    @GetMapping("/find/id")
    public String findUserByEmailForm() {
        return "users/findUserByEmail";
    }

    @PostMapping("/find/id")
    public String findUserByEmail(@RequestParam("name") String realName,
                                  @RequestParam("email") String email) throws MessagingException {
        Optional<User> userOpt = userService.findByRealNameAndEmail(realName, email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            emailService.sendUserIdEmail(user);

            return "redirect:/login";
        } else {
            return "redirect:/find/user";
        }
    }
}
