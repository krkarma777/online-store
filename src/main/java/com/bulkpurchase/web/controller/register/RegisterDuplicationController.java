package com.bulkpurchase.web.controller.register;

import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterDuplicationController {

    private final UserService userService;


    @PostMapping("/check-username")
    public ResponseEntity<?> checkUsernameAvailability(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        boolean isAvailable = !userService.existsByUsername(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isAvailable", isAvailable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/check-email")
    public ResponseEntity<?> checkEmailAvailability(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        boolean isAvailable = !userService.existsByEmail(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isAvailable", isAvailable);
        return ResponseEntity.ok(response);
    }
}
