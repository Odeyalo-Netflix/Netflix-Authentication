package com.odeyalo.analog.auth.service.oauth2.client.support.info;

import com.odeyalo.analog.auth.entity.enums.AuthProvider;

import java.util.Map;

public abstract class Oauth2UserInfo {
    protected Map<String, Object> attributes;

    public Oauth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAuthorities() {
        return attributes;
    }

    public void setAuthorities(Map<String, Object> attributes) {
        this.attributes = attributes;
    }


    public abstract String getEmail();

    public abstract String getFirstName();

    public abstract String getImage();


    public abstract AuthProvider getAuthProvider();
}
