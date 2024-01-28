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
                                .requestMatchers("/register", "/login", "/").permitAll()
                                .requestMatchers("/my/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/ADMIN").hasRole("ADMIN")
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
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}