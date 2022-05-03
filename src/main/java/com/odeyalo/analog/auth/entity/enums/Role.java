package com.odeyalo.analog.auth.entity.enums;

import org.springframework.security.core.GrantedAuthority;


public enum Role implements GrantedAuthority {
    ADMIN, MODERATOR, USER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
