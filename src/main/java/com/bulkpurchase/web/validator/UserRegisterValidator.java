package com.bulkpurchase.web.validator;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserRegisterValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        User user = (User) target;

        // 아이디 중복 검사
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            errors.rejectValue("username", "duplicate.username", "이미 사용 중인 아이디입니다.");
        }

        // 이메일 중복 검사
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            errors.rejectValue("email", "duplicate.email", "이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 길이 및 조합 검사
        if (!user.getPassword().matches("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[\\W_]).{8,20}$")) {
            errors.rejectValue("password", "invalid.password", "비밀번호는 영문, 숫자, 특수문자의 조합으로 8~20자여야 합니다.");
        }

        // 이름 검증
        if (user.getRealName() == null || user.getRealName().trim().isEmpty()) {
            errors.rejectValue("realName", "empty.realName", "이름을 입력해주세요.");
        }

        // 휴대폰 번호 검증
        if (user.getPhoneNumber() == null || !user.getPhoneNumber().matches("^\\d{10,11}$")) {
            errors.rejectValue("phoneNumber", "invalid.phoneNumber", "유효한 휴대폰 번호를 입력해주세요.");
        }

        // 주소 검증
        if (user.getAddress() == null || user.getAddress().trim().isEmpty()) {
            errors.rejectValue("address", "empty.address", "주소를 입력해주세요.");
        }
    }
}
