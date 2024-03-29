package com.odeyalo.analog.auth;

import com.odeyalo.analog.auth.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AuthApplicationTests extends AbstractIntegrationTest {
    @Autowired
    Environment environment;
    private final Logger logger = LoggerFactory.getLogger(AuthApplicationTests.class);

    @Test
    void contextLoads() {
        String property = environment.getProperty("spring.datasource.url");
        assertNotNull(property);
    }
}
