package com.odeyalo.analog.auth.exceptions;

public class QrCodeNotFoundException extends RuntimeException {
    public QrCodeNotFoundException() {
        super();
    }

    public QrCodeNotFoundException(String message) {
        super(message);
    }

    public QrCodeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
