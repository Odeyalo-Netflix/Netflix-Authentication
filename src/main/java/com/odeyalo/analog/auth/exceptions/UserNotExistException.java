package com.odeyalo.analog.auth.exceptions;

public class UserNotExistException extends LoginException {

    public UserNotExistException() {
        super();
    }

    public UserNotExistException(String message) {
        super(message);
    }

    public UserNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
