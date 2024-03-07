package com.bulkpurchase.web.controller.users.myPage.info;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.user.UserService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile/edit")
public class MyPageUserEditController {

    private final UserAuthValidator userAuthValidator;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping
    public String userEditForm(Model model, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        model.addAttribute("user", user);
        return "users/myPage/user_info/user_edit";
    }

    @PostMapping
    public String userEditForm(User user, Principal principal, @RequestParam("currentPassword") String currentPassword) {
        User existingUser = userAuthValidator.getCurrentUser(principal);
        if (bCryptPasswordEncoder.matches(currentPassword, existingUser.getPassword())) {
            existingUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            existingUser.setAddress(user.getAddress());
            existingUser.setDetailAddress(user.getDetailAddress());
            existingUser.setZipCode(user.getZipCode());
            userService.save(existingUser);
            return "redirect:/profile/edit?result=success";
        } else {
            return "redirect:/profile/edit?result=success";
        }
    }
}
