package com.odeyalo.analog.auth.exceptions;

import javax.security.auth.message.AuthException;

public class NicknameExistException extends AuthException {
    public NicknameExistException() {
    }

    public NicknameExistException(String msg) {
        super(msg);
    }
}
