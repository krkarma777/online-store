package com.bulkpurchase.web.validator.user;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserAuthValidator {

    private final UserService userService;

    public Optional<User> authenticate(Principal principal) {
        if (principal == null) {
            return Optional.empty();
        }
        return userService.findByUsername(principal.getName());
    }

    public User getCurrentUser(Principal principal) {
        return principal != null ? userService.findByUsername(principal.getName()).orElse(null) : null;
    }

    public boolean isProductOwner(Principal principal, Product product) {
        return userService.findByUsername(principal.getName())
                .map(user -> product.getUser().equals(user))
                .orElse(false);
    }

}
