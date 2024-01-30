package com.bulkpurchase.web.controller.users;

import com.bulkpurchase.domain.entity.User;
import com.bulkpurchase.domain.repository.UserRepository;
import com.bulkpurchase.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@Slf4j
public class MyPageController {

    private final UserService userService;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public MyPageController(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/mypage")
    public String myPageForm(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        model.addAttribute("user", user);
        return "users/myPage";
    }

    @GetMapping("/mypage/edit")
    public String userEditForm(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "users/edit";
    }
    @PostMapping("/mypage/update")
    public String userEditForm(@ModelAttribute User user, Principal principal) {
        log.info("user = {}" , user);

        String password = user.getPassword();
        String email = user.getEmail();
        User existingUser = userService.findByUsername(principal.getName());

        // 비밀번호가 비어 있지 않으면 해시 처리
        if (password != null && !password.isEmpty()) {
            String hashedPassword = passwordEncoder.encode(password);
            user.setPassword(hashedPassword);
        } else {
            // 기존 비밀번호 유지
            user.setPassword(existingUser.getPassword());
        }


        userRepository.save(user);
        return "redirect:/mypage";
    }
}
