package com.bulkpurchase.web.service.login;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.UserRole;
import com.bulkpurchase.domain.repository.user.UserRepository;
import com.bulkpurchase.domain.service.user.UserService;
import com.bulkpurchase.security.handler.CustomOAuth2User;
import com.bulkpurchase.security.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        Map<String, Object> attributes = user.getAttributes();

        // 메시지 상태 확인 후, 성공적으로 정보를 가져왔는지 검증
        if (!"success".equals(attributes.get("message").toString())) {
            throw new OAuth2AuthenticationException("OAuth2 공급자로부터 사용자 정보를 성공적으로 가져오지 못했습니다.");
        }

        // attributes에서 response를 추출하여 사용자 정보 설정
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        String email = response.get("email").toString();
        Optional<User> userOpt = userRepository.findByUsername(email);
        User userEntity = new User();
        if (userOpt.isEmpty()) {
            userEntity.setUsername(email); // 사용자 고유 이메일을 username으로 사용
            userEntity.setEmail(response.get("email").toString());
            userEntity.setRealName(response.get("name").toString());
            userEntity.setPhoneNumber(response.get("mobile").toString());
            userEntity.setRole(UserRole.ROLE_자영업자); // 모든 사용자를 자영업자로 설정
            userEntity.setPassword(UUID.randomUUID().toString());
            userRepository.save(userEntity);
        }

        // 필요한 정보를 바탕으로 JWT 생성 및 로그 출력
        jwtUtil.createJwt(response.get("id").toString(), "자영업자", 60 * 60 * 10L);

        return new CustomOAuth2User(user);
    }
}
