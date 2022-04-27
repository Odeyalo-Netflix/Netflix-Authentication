package com.odeyalo.analog.auth.service.register;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.service.oauth2.support.info.Oauth2UserInfo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.security.auth.message.AuthException;

public interface Oauth2RegisterHandler extends RegisterHandler {

    User register(Oauth2UserInfo user) throws AuthException;

    @Autowired
    default void addRegisterHandler(Oauth2RegisterHandlerFactory factory) {
        factory.addRegisterHandler(this.getAuthProvider(), this);
    }
}
