package com.bulkpurchase.web.controller.admin;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.UserStatus;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminUserManageController {

    private final UserService userService;

    @GetMapping("/user")
    public String userManagementPage(Model model) {
        List<User> users = userService.findAllOrderByUserID();
        model.addAttribute("users", users);

        return "/admin/userManagement";
    }

    @PostMapping("/user/status")
    @ResponseBody
    public ResponseEntity<?> changeStatus(@RequestParam("userID") Long userID,@RequestParam("status") UserStatus status) {
        User user = userService.findByUserid(userID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        user.setStatus(status);

        userService.save(user);

        return ResponseEntity.noContent().build();
    }
}
