package com.odeyalo.analog.auth.service.support;

import com.odeyalo.analog.auth.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class Oauth2UserDetails implements OAuth2User {
    protected Map<String, Object> attributes;

    public Oauth2UserDetails(User user) {

    }

    public static Oauth2UserDetails create(User user) {
        return new Oauth2UserDetails(user);
    }

    public static Oauth2UserDetails create(User user, Map<String, Object> attributes) {
        Oauth2UserDetails details = new Oauth2UserDetails(user);
        details.setAttributes(attributes);
        return details;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getName() {
        return "aboba";
    }
}
