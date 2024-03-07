package com.bulkpurchase.web.controller.users.register;

import com.bulkpurchase.domain.entity.user.RegisterCheck;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.user.RegisterService;
import com.bulkpurchase.domain.validator.user.UserRegisterValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@AllArgsConstructor
public class RegisterController {

    private final RegisterService registerService;
    private final UserRegisterValidator userRegisterValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(userRegisterValidator);
    }

    @PostMapping("/registerProc")
    public String registerProcess(@ModelAttribute @Validated(RegisterCheck.class) User user, BindingResult bindingResult, Model model) {

        userRegisterValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "/users/register";
        }

        registerService.registerProcess(user);
        return "redirect:/login";
    }
}