package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.user.UserStatusRequest;
import com.bulkpurchase.domain.entity.user.RegisterCheck;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.user.RegisterService;
import com.bulkpurchase.domain.service.user.UserService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import com.bulkpurchase.domain.validator.user.UserRegisterValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final RegisterService registerService;
    private final UserRegisterValidator userRegisterValidator;
    private final UserService userService;
    private final UserAuthValidator userAuthValidator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Validated(RegisterCheck.class) User user,
                                         BindingResult bindingResult) {
        userRegisterValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        registerService.registerProcess(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PatchMapping
    public ResponseEntity<?> update(@RequestBody User user,
                                    @RequestBody String currentPassword,
                                    Principal principal) {
        User userOrigin = userAuthValidator.getCurrentUser(principal);
        if (bCryptPasswordEncoder.matches(currentPassword, userOrigin.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userService.save(user);
            return ResponseEntity.ok().body(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Password does not match");
        }
    }

    @PatchMapping("/status")
    public ResponseEntity<?> changeStatus(@RequestBody UserStatusRequest userStatusRequest) {
        User user = userService.findByUserid(userStatusRequest.getUserID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        user.setStatus(userStatusRequest.getStatus());

        userService.save(user);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable Long userId) {
        if (!userService.existsById(userId)) {
            return ResponseEntity.notFound().build();
        }

        userService.deleteById(userId);
        return ResponseEntity.ok().build(); // 혹은 noContent().build()
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> findById(@PathVariable Long userId) {
        Optional<User> userOptional = userService.findByUserid(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userOptional.get());
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> allUsers = userService.findAll();
        return ResponseEntity.ok(allUsers);
    }
}
