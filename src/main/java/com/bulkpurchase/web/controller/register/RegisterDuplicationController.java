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
import java.util.function.Predicate;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterDuplicationController {

    private final UserService userService;

    @PostMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsernameAvailability(@RequestBody Map<String, String> request) {
        return checkAvailability(request.get("username"), userService::existsByUsername);
    }

    @PostMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmailAvailability(@RequestBody Map<String, String> request) {
        return checkAvailability(request.get("email"), userService::existsByEmail);
    }

    private ResponseEntity<Map<String, Boolean>> checkAvailability(String value, Predicate<String> existsByPredicate) {
        boolean isAvailable = !existsByPredicate.test(value);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isAvailable", isAvailable);
        return ResponseEntity.ok(response);
    }
}
