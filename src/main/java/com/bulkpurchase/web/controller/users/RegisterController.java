package com.bulkpurchase.web.controller.users;

import com.bulkpurchase.domain.entity.UserEntity;
import com.bulkpurchase.web.service.RegisterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@AllArgsConstructor
public class RegisterController {

    RegisterService registerService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userEntity", new UserEntity());
        return "/users/register";
    }

    @PostMapping("/registerProc")
    public String registerProcess(@ModelAttribute UserEntity userEntity) {
        registerService.registerProcess(userEntity);
        return "redirect:/login";
    }
}
