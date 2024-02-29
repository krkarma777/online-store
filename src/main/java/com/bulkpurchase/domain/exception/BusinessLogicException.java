package com.bulkpurchase.domain.exception;

import com.bulkpurchase.domain.enums.ExceptionCode;

import java.io.Serial;

public class BusinessLogicException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;


    public BusinessLogicException() {
        super();
    }

    public BusinessLogicException(ExceptionCode message) {
        super(message.getDescription());
    }

    public BusinessLogicException(String message) {
        super(message);
    }

    public BusinessLogicException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessLogicException(Throwable cause) {
        super(cause);
    }

    protected BusinessLogicException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
