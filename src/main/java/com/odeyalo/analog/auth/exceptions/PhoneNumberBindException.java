package com.odeyalo.analog.auth.exceptions;

public class PhoneNumberBindException extends RuntimeException {
    public PhoneNumberBindException() {
        super();
    }

    public PhoneNumberBindException(String message) {
        super(message);
    }

    public PhoneNumberBindException(String message, Throwable cause) {
        super(message, cause);
    }
}
