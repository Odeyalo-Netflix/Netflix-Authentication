package com.odeyalo.analog.auth.exceptions;

public class CodeVerificationException extends RuntimeException{

    public CodeVerificationException(String message) {
        super(message);
    }

    public CodeVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
