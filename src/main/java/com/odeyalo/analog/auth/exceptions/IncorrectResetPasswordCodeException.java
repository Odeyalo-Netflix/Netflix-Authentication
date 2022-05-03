package com.odeyalo.analog.auth.exceptions;

public class IncorrectResetPasswordCodeException extends RuntimeException {
    public IncorrectResetPasswordCodeException() {
        super();
    }

    public IncorrectResetPasswordCodeException(String message) {
        super(message);
    }

    public IncorrectResetPasswordCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
