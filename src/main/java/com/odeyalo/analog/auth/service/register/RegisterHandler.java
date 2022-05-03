package com.odeyalo.analog.auth.service.register;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.enums.AuthProvider;

import javax.security.auth.message.AuthException;

public interface RegisterHandler {

    User register(User user) throws AuthException;


    AuthProvider getAuthProvider();
}
