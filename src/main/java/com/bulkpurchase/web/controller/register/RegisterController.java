package com.bulkpurchase.web.controller.register;

import com.bulkpurchase.domain.entity.product.UpdateCheck;
import com.bulkpurchase.domain.entity.user.RegisterCheck;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.SalesRegion;
import com.bulkpurchase.domain.service.user.RegisterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
public class RegisterController {

    RegisterService registerService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "/users/register";
    }

    @PostMapping("/registerProc")
    public String registerProcess(@ModelAttribute@Validated(RegisterCheck.class) User user, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            model.addAttribute("user", user);
            return "/users/register";
        }

        // server side validation 추가해야함
        registerService.registerProcess(user);
        return "redirect:/login";
    }
}