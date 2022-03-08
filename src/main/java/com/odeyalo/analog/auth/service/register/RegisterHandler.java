package com.odeyalo.analog.auth.service.register;

import com.odeyalo.analog.auth.entity.User;

import javax.security.auth.message.AuthException;

public interface RegisterHandler {

    void register(User user) throws AuthException;

}
