package com.bulkpurchase.web.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserLoginService userLoginService;

    @Autowired
    public CustomUserDetailsService(UserLoginService userLoginService) {
        this.userLoginService = userLoginService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.bulkpurchase.domain.model.User user = userLoginService.loginUser(username, null);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new User(user.getUsername(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
