package com.odeyalo.analog.auth.config.security.jwt.utils;

import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.exceptions.JwtParserConstructionException;
import com.odeyalo.analog.auth.service.support.CustomUserDetails;
import com.odeyalo.analog.auth.utils.TestUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple tests for
 * @see RsaTokenPairJwtTokenProvider
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RsaTokenPairJwtTokenProviderTest {
    private static final short EXPECTED_USER_ID = 1;
    private final RsaTokenPairJwtTokenProvider provider;
    private String JWT_TOKEN;
    private CustomUserDetails details;
    private static final String INVALID_JWT_TOKEN = "INVALID_JWT_TOKEN";
    private static final String EXPECTED_JWT_USERNAME = "generated123";
    private static final String EXPECTED_USER_NICKNAME = "generated123";
    private static final String EXPECTED_JWT_EMAIL = "generated@gmail.com";
    private final List<String> EXPECTED_USER_ROLES = Collections.singletonList(Role.USER.getAuthority());;

    RsaTokenPairJwtTokenProviderTest() throws NoSuchAlgorithmException, JwtParserConstructionException {
        RsaTokenPairGenerator generator = new RsaTokenPairGeneratorImpl();
        Pair<PublicKey, PrivateKey> rsaTokens = generator.getRsaTokens();
        this.provider = new RsaTokenPairJwtTokenProvider(rsaTokens);
        this.provider.init();
    }

    @BeforeAll
    void setUp() {
        ReflectionTestUtils.setField(provider, "JWT_TOKEN_EXPIRATION_TIME", 600, Integer.class);
        this.details = new CustomUserDetails(TestUtils.buildGeneratedUser(1));
        this.JWT_TOKEN = this.provider.generateJwtToken(this.details);
    }

    @Test
    void generateJwtToken() {
        String token = provider.generateJwtToken(this.details);
        assertNotNull(token);
    }

    @Test
    void isTokenValid() {
        boolean tokenValid = provider.isTokenValid(JWT_TOKEN, this.details);
        assertTrue(tokenValid);
    }

    @Test
    void isNicknameFromTokenValid() {
        String nickname = this.provider.getNicknameFromJwtToken(JWT_TOKEN);
        assertNotNull(nickname);
        assertEquals(EXPECTED_JWT_USERNAME, nickname);
    }

    @Test
    void validateTokenTest() {
        boolean validateToken = this.provider.validateToken(JWT_TOKEN);
        assertTrue(validateToken);
    }

    @Test
    void getBodyTest() {
        Claims body = (Claims) this.provider.getBody(JWT_TOKEN);
        Integer id = body.get("id",Integer.class);
        System.out.println(body);
        List<String> roles = (List<String>) body.get("roles");
        String username = body.get("username", String.class);
        String nickname = body.get("nickname", String.class);
        assertNotNull(body);
        assertArrayEquals(EXPECTED_USER_ROLES.toArray(), roles.toArray());
        assertEquals(EXPECTED_USER_ID, id);
        assertEquals(EXPECTED_JWT_USERNAME, username);
        assertEquals(EXPECTED_USER_NICKNAME, nickname);
    }
}
