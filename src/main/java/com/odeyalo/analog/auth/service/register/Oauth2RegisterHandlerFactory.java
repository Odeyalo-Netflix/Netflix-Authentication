package com.odeyalo.analog.auth.service.register;

import com.odeyalo.analog.auth.entity.enums.AuthProvider;
import com.odeyalo.analog.auth.service.oauth2.support.info.Oauth2UserInfo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Oauth2RegisterHandlerFactory {
    private final static Map<AuthProvider, Oauth2RegisterHandler> registerHandlers = new HashMap<>(5);

    public static Oauth2RegisterHandler getOauth2RegisterHandler(Oauth2UserInfo info) {
        AuthProvider authProvider = info.getAuthProvider();
        return Oauth2RegisterHandlerFactory.registerHandlers.get(authProvider);
    }

    public void addRegisterHandler(AuthProvider provider, Oauth2RegisterHandler handler) {
        Oauth2RegisterHandlerFactory.registerHandlers.put(provider, handler);
    }
}
