package com.bulkpurchase.web.service.login;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.UserRole;
import com.bulkpurchase.security.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        // OAuth2 공급자로부터 받은 사용자 정보를 기반으로 추가 작업을 수행합니다.
        // 예를 들어, 사용자 정보를 데이터베이스에 저장하거나, 사용자별 권한을 설정할 수 있습니다.
        System.out.println("user = " + user);

        String username = user.getName();
        UserRole userRole = UserRole.ROLE_자영업자;
        // 필요한 정보를 바탕으로 JWT 생성
        String token = jwtUtil.createJwt(username, userRole.toString(), 60*60*10L);

        // JWT 토큰을 사용자 세션에 저장하거나, 적절한 방법으로 클라이언트에 전달합니다.
//        response.addHeader("Authorization", "Bearer " + token);
//        response.sendRedirect("/");


        Map<String, Object> attributes = user.getAttributes();
        if (attributes.get("message").toString().equals("success")) {
            return null;
        }
        User userEntity = new User();
//        userEntity.setUsername();
        userEntity.setRealName(attributes.get("name").toString());


        return user;
    }
}
