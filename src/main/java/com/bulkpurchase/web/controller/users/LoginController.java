package com.bulkpurchase.web.controller.users;

import com.bulkpurchase.domain.model.User;
import com.bulkpurchase.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@AllArgsConstructor
public class LoginController {

    private final UserService userService;

    // 회원가입 폼을 표시하는 메소드
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); // 폼에 바인딩할 빈 User 객체를 모델에 추가
        return "/users/register"; // Thymeleaf 템플릿 이름 (예: "register.html")
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userService.registerUser(user);
        return "redirect:/login"; // 가입 후 로그인 페이지로 리다이렉트
    }
}