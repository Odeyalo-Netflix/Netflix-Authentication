package com.odeyalo.analog.auth.unit.service.refresh;

import com.odeyalo.analog.auth.entity.RefreshToken;
import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.exceptions.RefreshTokenException;
import com.odeyalo.analog.auth.repository.RefreshTokenRepository;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.service.refresh.DefaultRefreshTokenHandler;
import com.odeyalo.analog.auth.service.refresh.RefreshTokenGenerator;
import com.odeyalo.analog.auth.service.refresh.UUIDRefreshTokenGenerator;
import com.odeyalo.analog.auth.utils.TestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultRefreshTokenHandlerTest {
    @Autowired
    private DefaultRefreshTokenHandler refreshTokenHandler;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenGenerator generator = new UUIDRefreshTokenGenerator();
    private User user;

    @BeforeAll
    void setUp() {
        this.user = this.userRepository.save(TestUtils.buildUser("email@mail.ua", "nickname", "123", false, Role.USER));
    }

    @BeforeEach
    void beforeEach() {
        String token = this.generator.generate();
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(token)
                .expireDate(Instant.now().plusSeconds(30))
                .user(this.user)
                .build();
        this.refreshTokenRepository.save(refreshToken);
    }

    @Test
    void createAndSaveToken() {
        this.refreshTokenHandler.createAndSaveToken(this.user);
        int size = this.refreshTokenRepository.findAll().size();
        assertEquals(2, size);
    }

    @Test
    @DisplayName("Verify expired token and except RefreshTokenException")
    void verifyExpiredToken() {
        RefreshToken build = RefreshToken.builder()
                .id(10)
                .refreshToken(this.generator.generate())
                .user(this.user)
                .expireDate(Instant.now().minusSeconds(10))
                .build();
        assertThrows(RefreshTokenException.class, () -> this.refreshTokenHandler.verifyToken(build));
    }
    @Test
    @DisplayName("Verify not expired token and except RefreshTokenException")
    void verifyNotExpiredToken() {
        RefreshToken build = RefreshToken.builder()
                .id(10)
                .refreshToken(this.generator.generate())
                .user(this.user)
                .expireDate(Instant.now().plusSeconds(10))
                .build();
        assertTrue(this.refreshTokenHandler.verifyToken(build));
    }

    @Test
    @DisplayName("Delete  refresh token by existed user id")
    void deleteTokenByExistedUserId() {
        this.refreshTokenHandler.deleteTokenByUserId(this.user.getId());
        int size = this.refreshTokenRepository.findAll().size();
        assertEquals(0, size);
    }
    @Test
    @DisplayName("Delete refresh token by not existed user id")
    void deleteTokenByNotExistedUserId() {
        this.refreshTokenHandler.deleteTokenByUserId(-1);
        int size = this.refreshTokenRepository.findAll().size();
        assertEquals(1, size);
    }

    @AfterEach
    void afterEach() {
        this.refreshTokenRepository.deleteAll();
    }

    @AfterAll
    void clear() {
        this.userRepository.deleteAll();
    }
}