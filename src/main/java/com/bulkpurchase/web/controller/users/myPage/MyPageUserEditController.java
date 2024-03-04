package com.bulkpurchase.web.controller.users.myPage;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.user.UserService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/myPage")
public class MyPageUserEditController {

    private final UserAuthValidator userAuthValidator;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @GetMapping("/edit")
    public String userEditForm(Model model, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        model.addAttribute("user", user);
        return "users/edit";
    }

    @PostMapping("/update")
    public String userEditForm(@ModelAttribute User user, Principal principal) {
        String password = user.getPassword();
        User existingUser = userAuthValidator.getCurrentUser(principal);

        // 비밀번호가 비어 있지 않으면 해시 처리
        if (password != null && !password.isEmpty()) {
            String hashedPassword = passwordEncoder.encode(password);
            user.setPassword(hashedPassword);
        } else {
            // 기존 비밀번호 유지
            user.setPassword(existingUser.getPassword());
        }

        userService.save(user);
        return "redirect:/mypage";
    }
}
