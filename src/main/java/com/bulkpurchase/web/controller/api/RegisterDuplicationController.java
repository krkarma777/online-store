package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RegisterDuplicationController {

    private final UserService userService;

    @GetMapping("/username-check")
    public ResponseEntity<Map<String, Boolean>> checkUsernameAvailability(@RequestParam("username") String username) {
        return checkAvailability(username, userService::existsByUsername);
    }

    @GetMapping("/email-check")
    public ResponseEntity<Map<String, Boolean>> checkEmailAvailability(@RequestParam("email") String email) {
        return checkAvailability(email, userService::existsByEmail);
    }

    private ResponseEntity<Map<String, Boolean>> checkAvailability(String value, Predicate<String> existsByPredicate) {
        boolean isAvailable = !existsByPredicate.test(value);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isAvailable", isAvailable);
        return ResponseEntity.ok(response);
    }
}
