package com.odeyalo.analog.auth.exceptions;

public class FileNotPresentException extends RuntimeException {
    public FileNotPresentException() {
        super();
    }

    public FileNotPresentException(String message) {
        super(message);
    }

    public FileNotPresentException(String message, Throwable cause) {
        super(message, cause);
    }
}
