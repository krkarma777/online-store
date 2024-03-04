package com.bulkpurchase.web.controller.users;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.user.UserService;
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

    @GetMapping("/find/user")
    public String findUserByEmailForm() {
        return "users/findUserByEmail";
    }

    @PostMapping("/find/id")
    public String findUserByEmail(@RequestParam("name") String realName,
                                  @RequestParam("email") String email) {
        Optional<User> userOpt = userService.findByRealNameAndEmail(realName, email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            log.info("user = {}", user);

            return "users/findUserByEmail";
        } else {
            return "redirect:/";
        }
    }
}
