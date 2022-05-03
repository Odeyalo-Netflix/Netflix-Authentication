package com.odeyalo.analog.auth.unit.refresh;

import com.odeyalo.analog.auth.service.refresh.UUIDRefreshTokenGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class UUIDRefreshTokenGeneratorTest {
    private final UUIDRefreshTokenGenerator generator = new UUIDRefreshTokenGenerator();

    @Test
    void generate() {
        String uuid = generator.generate();
        assertNotNull(uuid);
    }
}
