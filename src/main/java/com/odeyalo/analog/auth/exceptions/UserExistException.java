package com.odeyalo.analog.auth.exceptions;

import javax.security.auth.message.AuthException;

public class UserExistException extends AuthException {

    public UserExistException() {
        super();
    }

    public UserExistException(String msg) {
        super(msg);
    }
}
