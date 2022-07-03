package com.odeyalo.analog.auth.service.oauth2.client.support.info;

import com.odeyalo.analog.auth.entity.enums.AuthProvider;

import java.util.Map;

public class GoogleOauth2UserInfo extends Oauth2UserInfo {

    public GoogleOauth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getEmail() {
        return (String) this.attributes.get("email");
    }

    @Override
    public String getFirstName() {
        return (String) this.attributes.get("name");
    }

    @Override
    public String getImage() {
        return (String) this.attributes.get("picture");
    }

    @Override
    public AuthProvider getAuthProvider() {
        return AuthProvider.GOOGLE;
    }
}
