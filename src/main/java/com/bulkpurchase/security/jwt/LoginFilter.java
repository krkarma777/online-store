package com.bulkpurchase.security.jwt;

import com.bulkpurchase.domain.dto.user.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final Long expiredMs;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(username, role, expiredMs);
        String cookieValue = "AuthToken=" + token + "; Path=/; HttpOnly";
        if (request.isSecure()) { // HTTPS인 경우에만 Secure 플래그 추가
            cookieValue += "; Secure";
        }
        response.addHeader("Set-Cookie", cookieValue);
        response.sendRedirect("/");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String username = obtainUsername(request); // 로그인 시도한 사용자 이름 가져오기
        String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8); // URL에 포함시키기 위해 인코딩

        if (Objects.equals(exception.getMessage(), "이메일 인증이 필요합니다.")) {
            // 이메일 인증이 필요한 경우의 로직
            response.sendRedirect("/verify?username=" + encodedUsername); // 이메일 인증 페이지로 리디렉션
        } else if (Objects.equals(exception.getMessage(), "차단된 사용자입니다.")) {
            response.sendRedirect("/banned");
        } else {
            // 다른 종류의 인증 실패 처리
            response.sendRedirect("/login?error=true"); // 기본 로그인 페이지로 리디렉션
        }
    }
}
