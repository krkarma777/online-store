package com.bulkpurchase.domain.exception;

import org.springframework.security.core.AuthenticationException;

import java.io.Serial;

public class UserLoginAuthenticatedException extends AuthenticationException {

    @Serial
    private static final long serialVersionUID = 1L;


    public UserLoginAuthenticatedException(String message) {
        super(message);
    }

    public UserLoginAuthenticatedException(String message, Throwable cause) {
        super(message, cause);
    }

}
