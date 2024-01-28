package com.bulkpurchase.web.controller.users;

import com.bulkpurchase.domain.model.User;
import com.bulkpurchase.web.service.UserLoginService;
import com.bulkpurchase.web.service.UserRegisterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@AllArgsConstructor
@Slf4j
public class LoginController {

    private final UserRegisterService userRegisterService;

    private final UserLoginService userLoginService;

    // 회원가입 폼을 표시하는 메소드
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); // 폼에 바인딩할 빈 User 객체를 모델에 추가
        return "/users/register"; // Thymeleaf 템플릿 이름 (예: "register.html")
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userRegisterService.registerUser(user);
        return "redirect:/login"; // 가입 후 로그인 페이지로 리다이렉트
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "/users/login";
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam(name = "username") String username,
                                       @RequestParam(name = "password") String password) {

        log.info("username= {}",username);
        log.info("password= {}",password);
        User user = userLoginService.loginUser(username, password);
        log.info("user= {}",user);


        if (user != null) {
            // 로그인 성공 처리
            return ResponseEntity.ok().body("로그인 성공");
        } else {
            // 로그인 실패 처리
            return ResponseEntity.badRequest().body("로그인 실패: 사용자 이름 또는 비밀번호가 잘못되었습니다.");
        }
    }
}