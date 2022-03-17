package com.odeyalo.analog.auth.service.support;

import com.odeyalo.analog.auth.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Map;

@Component
public class CustomUserDetails implements OAuth2User, UserDetails {
    protected  User user;
    protected Map<String, Object> attributes;

    public CustomUserDetails() {}

    public CustomUserDetails(User user) {
        this.user = user;
    }


    public static CustomUserDetails create(User user, Map<String, Object> attributes) {
        CustomUserDetails details = new CustomUserDetails(user);
        details.setAttributes(attributes);
        return details;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user.getRoles();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getNickname();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.isAccountActivated();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !this.user.isUserBanned();
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getName() {
        return this.getUsername();
    }
}
