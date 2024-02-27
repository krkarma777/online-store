package com.bulkpurchase.domain.service.user.oauth2;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.UserRole;
import com.bulkpurchase.domain.repository.user.UserRepository;
import com.bulkpurchase.security.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NaverLoginService implements SocialOauth2Service{

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    @Value("${jwt.expiredMs}") private String expiredMs;

    @Override
    public String login(Map<String, Object> attributes) {

        // 메시지 상태 확인 후, 성공적으로 정보를 가져왔는지 검증
        if (!"success".equals(attributes.get("message").toString())) {
            throw new OAuth2AuthenticationException("OAuth2 공급자로부터 사용자 정보를 성공적으로 가져오지 못했습니다.");
        }
        // attributes에서 response를 추출하여 사용자 정보 설정
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        String email = response.get("email").toString();
        Optional<User> userOpt = userRepository.findByUsername(email);
        String role = "자영업자";
        User userEntity = new User();
        if (userOpt.isEmpty()) {
            userEntity.setUsername(email); // 사용자 고유 이메일을 username으로 사용
            userEntity.setEmail(response.get("email").toString());
            userEntity.setPhoneNumber(response.get("mobile").toString());
            userEntity.setRealName(response.get("name").toString());
            userEntity.setRole(UserRole.ROLE_자영업자); // 모든 사용자를 자영업자로 설정
            userEntity.setPassword(UUID.randomUUID().toString());
            userRepository.save(userEntity);
        } else {
            String gettedRole = userOpt.get().getRole().toString();
            UserRole userRole = UserRole.fromRoleString(gettedRole);
            role = userRole.getDescription();
        }
        return jwtUtil.createJwt(response.get("id").toString(), role, Long.parseLong(expiredMs));
    }
}
