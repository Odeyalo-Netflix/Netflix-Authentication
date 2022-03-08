package com.odeyalo.analog.auth.service.refresh;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDRefreshTokenGenerator implements RefreshTokenGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
