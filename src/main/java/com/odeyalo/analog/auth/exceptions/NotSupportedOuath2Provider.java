package com.odeyalo.analog.auth.exceptions;

public class NotSupportedOuath2Provider extends RuntimeException {
    public NotSupportedOuath2Provider() {
        super();
    }

    public NotSupportedOuath2Provider(String message) {
        super(message);
    }

    public NotSupportedOuath2Provider(String message, Throwable cause) {
        super(message, cause);
    }
}
