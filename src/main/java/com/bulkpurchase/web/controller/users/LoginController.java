package com.bulkpurchase.web.controller.users;

import com.bulkpurchase.domain.entity.UserEntity;
import com.bulkpurchase.domain.service.LoginService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@AllArgsConstructor
public class LoginController {

    LoginService loginService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "/users/login";
    }


    @PostMapping("/loginProc")
    public ResponseEntity<?> loginUser(@RequestParam(name = "username") String username,
                                       @RequestParam(name = "password") String password) {


        log.info("username={}",username);
        log.info("password={}",password);
        UserEntity user = loginService.loginUser(username, password);
        log.info("username={}",user);

        if (user != null) {
            // 로그인 성공 처리
            return ResponseEntity.ok().body("로그인 성공");
        } else {
            // 로그인 실패 처리
            return ResponseEntity.badRequest().body("로그인 실패: 사용자 이름 또는 비밀번호가 잘못되었습니다.");
        }
    }
}