package com.bulkpurchase.web.controller.admin;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminUserManageController {

    private final UserService userService;

    @GetMapping("/user")
    public String userManagementPage(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);

        return "/admin/userManagement";
    }
}
