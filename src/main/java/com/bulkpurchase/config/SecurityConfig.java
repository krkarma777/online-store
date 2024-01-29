package com.bulkpurchase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeHttpRequests(
                        (authorize) -> authorize
                                .requestMatchers("/register", "/registerProc", "/login", "/", "/loginProc").permitAll()
                                .requestMatchers("/my/**").hasAnyRole("판매자", "관리자")
                                .requestMatchers("/ADMIN").hasRole("관리자")
                        .anyRequest().authenticated()
                );

        httpSecurity
                .formLogin((authorize) -> authorize
                        .loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .permitAll()
                );

        httpSecurity
                .csrf((authorize) -> authorize.disable()
                );

        return httpSecurity.build();
    }

}