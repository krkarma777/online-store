package com.bulkpurchase.security.handler;

import com.bulkpurchase.domain.enums.UserRole;
import com.bulkpurchase.security.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 사용자 정보를 바탕으로 JWT 토큰 생성
        String username = authentication.getName(); // OAuth2 로그인의 경우, 이 메소드로 사용자의 고유 이름(아이디)를 가져올 수 있습니다.
        UserRole role = UserRole.fromRoleString("ROLE_자영업자");
        long validityInMilliseconds = 60*60*10L; // 토큰 유효 시간 (예: 1시간)

        String token = jwtUtil.createJwt(username, role.toString(), validityInMilliseconds);

        // 토큰을 HTTP 응답 헤더에 추가
        String cookieValue = "AuthToken=" + token + "; Path=/; HttpOnly";
        if (request.isSecure()) { // HTTPS인 경우에만 Secure 플래그 추가
            cookieValue += "; Secure";
        }
        response.addHeader("Set-Cookie", cookieValue);
        response.sendRedirect("/");
    }
}
