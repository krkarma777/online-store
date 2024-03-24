package com.bulkpurchase.domain.validator.user;

import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserAuthValidator {

    private final UserService userService;

    public Optional<User> authenticate(Principal principal) {
        if (principal == null) {
            return Optional.empty();
        }
        return userService.findByUsername(principal.getName());
    }

    public User getCurrentUser(Principal principal) {
        return userService.findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }
    public User getCurrentUserByUsername(String username) {
        return userService.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    public User getCurrentUserByUserID(Long sellerID) {
        return userService.findByUserid(sellerID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    public boolean isProductOwner(Principal principal, Product product) {
        return userService.findByUsername(principal.getName())
                .map(user -> product.getUser().equals(user))
                .orElse(false);
    }

    public void validateUserAccessOrderDetail(User user, OrderDetail orderDetail) {
        if (!orderDetail.getProduct().getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "주문 정보에 접근할 권한이 없습니다.");
        }
    }
}
