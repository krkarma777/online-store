package com.bulkpurchase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeHttpRequests(
                        (authorize) -> authorize
                                .requestMatchers("/css/**", "/js/**", "/img/**").permitAll() // 정적 리소스 허용
                                .requestMatchers("/register/**", "/registerProc", "/login/**", "/loginProc", "/").permitAll()
                                .requestMatchers("/mypage/**","/cart/**").authenticated()
                                .requestMatchers("/product/add", "/seller/**").hasAnyRole("판매자", "관리자")
                                .requestMatchers("/admin/**").hasRole("관리자")
                                .requestMatchers("/*").permitAll()
                                .anyRequest().authenticated()
                )

                .formLogin((authorize) -> authorize
                        .loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .permitAll()
                )

                        .logout((logoutConfig) ->
                        logoutConfig
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/main")
                                .invalidateHttpSession(true)
                                .deleteCookies("JESSIONID")
                                .permitAll())

                .csrf(AbstractHttpConfigurer::disable
                );
        return httpSecurity.build();
    }

}