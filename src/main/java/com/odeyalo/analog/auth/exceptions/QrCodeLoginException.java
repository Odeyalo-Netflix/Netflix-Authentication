package com.odeyalo.analog.auth.exceptions;

public class QrCodeLoginException extends RuntimeException {

    public QrCodeLoginException() {
    }

    public QrCodeLoginException(String message) {
        super(message);
    }

    public QrCodeLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
