package com.odeyalo.analog.auth.exceptions;

import javax.security.auth.message.AuthException;

public class EmailExistException extends AuthException {

    public EmailExistException() {
        super();
    }

    public EmailExistException(String msg) {
        super(msg);
    }
}
