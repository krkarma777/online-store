package com.bulkpurchase.domain.service.user.oauth2;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.UserRole;
import com.bulkpurchase.domain.repository.user.UserRepository;
import com.bulkpurchase.security.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KakaoLoginService implements SocialOauth2Service{

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    @Value("${jwt.expiredMs}") private String expiredMs;

    @Override
    public void login(Map<String, Object> attributes) {

        User userEntity = new User();
        String username = attributes.get("id").toString();
        Optional<User> kakaoUserOpt = userRepository.findByUsername(username);
        if (kakaoUserOpt.isEmpty()) {
            userEntity.setUsername(username);
            userEntity.setRealName(attributes.get("id").toString());
            userEntity.setEmail(UUID.randomUUID().toString());
            userEntity.setPhoneNumber("01000000000");
            userEntity.setRole(UserRole.ROLE_자영업자); // 모든 사용자를 자영업자로 설정
            userEntity.setPassword(UUID.randomUUID().toString());
            userRepository.save(userEntity);
        }

        // 필요한 정보를 바탕으로 JWT 생성 및 로그 출력
        jwtUtil.createJwt(username, "자영업자", Long.parseLong(expiredMs));
    }
}
