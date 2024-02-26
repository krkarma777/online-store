package com.bulkpurchase.security.handler;

import com.bulkpurchase.domain.dto.user.oauth2.KakaoOAuth2User;
import com.bulkpurchase.domain.dto.user.oauth2.NaverOAuth2User;
import com.bulkpurchase.domain.enums.UserRole;
import com.bulkpurchase.security.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            String token = null;

            if (oauth2User instanceof NaverOAuth2User naverUser) {
                token = naverUser.getToken();
            } else if (oauth2User instanceof KakaoOAuth2User kakaoUser) {
                token = kakaoUser.getToken();
            }

            if (token != null) {
                String cookieValue = "AuthToken=" + token + "; Path=/; HttpOnly";
                if (request.isSecure()) { // 여기에서 요청이 안전한지 확인합니다.
                    cookieValue += "; Secure";
                }
                response.addHeader("Set-Cookie", cookieValue);
                response.sendRedirect("/");
            }
        }
    }
}
