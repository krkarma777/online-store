package com.bulkpurchase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeHttpRequests(
                        (authorize) -> authorize
                                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // 정적 리소스 허용
                                .requestMatchers("/register", "/registerProc", "/login", "/loginProc", "/").permitAll()
                                .requestMatchers("/mypage","/mypage/update" , "/mypage/edit" ,"/cart", "/cart/add").authenticated()
                                .requestMatchers("/product/add").hasAnyRole("판매자", "관리자")
                                .requestMatchers("/admin").hasRole("관리자")
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

                .csrf((authorize) ->
                        authorize.disable()
                );
        return httpSecurity.build();
    }

}