package com.odeyalo.analog.auth.service.oauth2.client.support;

import com.odeyalo.analog.auth.entity.enums.AuthProvider;
import com.odeyalo.analog.auth.exceptions.NotSupportedOuath2Provider;
import com.odeyalo.analog.auth.service.oauth2.client.support.info.GoogleOauth2UserInfo;
import com.odeyalo.analog.auth.service.oauth2.client.support.info.Oauth2UserInfo;

import java.util.Map;

import static java.lang.String.format;

public class OAuth2UserInfoFactory {

    public static Oauth2UserInfo getOauth2User(String authProvider, Map<String, Object> attributes) {
        if (authProvider.equalsIgnoreCase(AuthProvider.GOOGLE.toString())) {
            return new GoogleOauth2UserInfo(attributes);
        }
        throw new NotSupportedOuath2Provider(format("%s not supported", authProvider));
    }
}
