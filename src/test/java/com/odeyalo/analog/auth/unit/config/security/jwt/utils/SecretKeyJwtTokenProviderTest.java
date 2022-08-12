package com.odeyalo.analog.auth.unit.config.security.jwt.utils;

import com.odeyalo.analog.auth.config.security.jwt.utils.SecretKeyJwtTokenProvider;
import com.odeyalo.analog.auth.service.support.CustomUserDetails;
import com.odeyalo.analog.auth.utils.TestUtils;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SecretKeyJwtTokenProviderTest {
    SecretKeyJwtTokenProvider provider = new SecretKeyJwtTokenProvider("secret");
    private CustomUserDetails details;
    private String JWT_TOKEN;
    private static final String INVALID_JWT_TOKEN = "INVALID_JWT_TOKEN";
    private static final String EXPECTED_JWT_USERNAME = "generated123";
    private static final String EXPECTED_JWT_EMAIL = "generated@gmail.com";
    private static final String EXPIRED_JWT_TOKEN =
            "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJnZW5lcmF0ZWQxMjMiLCJyb2xlcyI6WyJVU0VSIl0sIm5pY2tuYW1lIjoiZ2VuZXJhdGVkMTIzIiwiaWQiOjEsImV4cCI6MTY2MDIwNTkzMSwidXNlcm5hbWUiOiJnZW5lcmF0ZWQxMjMifQ.sLlM8vrBA_QTMJEVzJRoT9-xjMOdIhV4wkrIOz6L71RTxiPIqNfuryno_LQlqZM06-424dSWzmgG49YOdHX_dg";

    @BeforeAll
    void setUp() {
        ReflectionTestUtils.setField(provider, "JWT_TOKEN_EXPIRATION_TIME", 600, Integer.class);
        this.details = new CustomUserDetails(TestUtils.buildGeneratedUser(1));
        this.JWT_TOKEN = this.provider.generateJwtToken(this.details);
    }

    @Test
    void generateJwtToken() {
        String jwtToken = this.provider.generateJwtToken(this.details);
        assertNotNull(jwtToken);
    }

    @Test
    @DisplayName("is correct token valid. expect true")
    void isCorrectTokenValid() {
        boolean tokenValid = this.provider.isTokenValid(JWT_TOKEN, this.details);
        assertTrue(tokenValid);
    }

    @Test
    @DisplayName("is expired token valid. expect true")
    void isExpiredTokenValid() {
        assertFalse(this.provider.isTokenValid(EXPIRED_JWT_TOKEN, this.details));
    }

    @Test
    void getNicknameFromJwtToken() {
        String nickname = this.provider.getNicknameFromJwtToken(JWT_TOKEN);
        assertNotNull(nickname);
        assertEquals(EXPECTED_JWT_USERNAME, nickname);
    }

    @Test
    void validateInvalidToken() {
        boolean b = this.provider.validateToken(INVALID_JWT_TOKEN);
        assertFalse(b);
    }

    @Test
    void validateCorrectToken() {
        boolean b = this.provider.validateToken(JWT_TOKEN);
        assertTrue(b);
    }

    @Test
    void validateExpiredToken() {
        boolean b = this.provider.validateToken(EXPIRED_JWT_TOKEN);
        assertFalse(b);
    }

    @Test
    void getBody() {
        Object body = this.provider.getBody(JWT_TOKEN);
        assertNotNull(body);
    }

    @Test
    void getClaims() {
        Claims claims = this.provider.getClaims(JWT_TOKEN);
        assertNotNull(claims);
    }

    @Test
    void isCorrectTokenExpired() {
        boolean tokenExpired = this.provider.isTokenExpired(JWT_TOKEN);
        assertFalse(tokenExpired);
    }

    @Test
    void isExpiredTokenExpired() {
        boolean tokenExpired = this.provider.isTokenExpired(EXPIRED_JWT_TOKEN);
        assertTrue(tokenExpired);
    }
}
