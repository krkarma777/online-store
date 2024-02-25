package com.bulkpurchase.security;

import com.bulkpurchase.security.handler.CustomAuthenticationSuccessHandler;
import com.bulkpurchase.security.handler.LoginAuthenticationFailureHandler;
import com.bulkpurchase.security.jwt.JWTFilter;
import com.bulkpurchase.security.jwt.JWTUtil;
import com.bulkpurchase.security.jwt.LoginFilter;
import com.bulkpurchase.web.service.login.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        http
                .authorizeHttpRequests(
                        (authorize) -> authorize
                                .requestMatchers("/", "/api/v1/auth/**", "/oauth2/**", "/inquiries/load-more").permitAll()
                                .requestMatchers("/css/**", "/js/**", "/img/**").permitAll() // 정적 리소스 허용
                                .requestMatchers("/register/**", "/registerProc", "/login/**", "/loginProc", "/").permitAll()
                                .requestMatchers("/mypage/**", "/cart/**", "/review/write", "/product/inquiry/add/**", "/review/*/feedback", "/order/**",
                                        "/favorite/**", "/coupon/**", "/user/**").authenticated()
                                .requestMatchers("/product/add", "/seller/**").hasAnyRole("판매자", "관리자")
                                .requestMatchers("/admin/**").hasRole("관리자")
/*                                .requestMatchers("/*").permitAll()*/
                                .anyRequest().permitAll()
                );
        http
                .oauth2Login(oauth2 -> oauth2
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
                        .userInfoEndpoint(endpoint -> endpoint.userService(customOAuth2UserService))
                        .successHandler(customAuthenticationSuccessHandler)
                );

//                .formLogin((authorize) -> authorize
//                                .disable()
//                        .loginPage("/login")
//                        .loginProcessingUrl("/loginProc")
//                        .successHandler((HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
//
//                        })
//                        .failureHandler(new LoginAuthenticationFailureHandler()).permitAll())
//
        http
                .logout((logoutConfig) ->
                        logoutConfig
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                                .invalidateHttpSession(true)
                                .deleteCookies("AuthToken")
                                .permitAll());
        //csrf disable
        http
                .csrf(AbstractHttpConfigurer::disable);

        //Form 로그인 방식 disable
        http
                .formLogin(AbstractHttpConfigurer::disable);

        //http basic 인증 방식 disable
        http
                .httpBasic(AbstractHttpConfigurer::disable);

        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}