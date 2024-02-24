package com.bulkpurchase.security;

import com.bulkpurchase.security.handler.LoginAuthenticationFailureHandler;
import com.bulkpurchase.security.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

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
                )

/*                .oauth2Login(oauth2 -> oauth2
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
                        .userInfoEndpoint(endpoint -> endpoint.userService(defaultOAuth2UserService))
                )*/

                .formLogin((authorize) -> authorize
                        .loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .failureHandler(new LoginAuthenticationFailureHandler())
                        .permitAll()
                )

                .logout((logoutConfig) ->
                        logoutConfig
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/main")
                                .invalidateHttpSession(true)
                                .deleteCookies("JESSIONID")
                                .permitAll())

                .csrf(AbstractHttpConfigurer::disable)

                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}