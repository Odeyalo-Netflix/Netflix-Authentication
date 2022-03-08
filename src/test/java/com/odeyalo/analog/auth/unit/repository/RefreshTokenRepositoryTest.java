package com.odeyalo.analog.auth.unit.repository;

import com.odeyalo.analog.auth.entity.RefreshToken;
import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.repository.RefreshTokenRepository;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.service.refresh.RefreshTokenGenerator;
import com.odeyalo.analog.auth.service.refresh.UUIDRefreshTokenGenerator;
import com.odeyalo.analog.auth.unit.config.CoreTestConfiguration;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RefreshTokenRepositoryTest {
    private static final Integer NOT_EXISTED_USER_ID = -1;
    private static final String NOT_EXISTED_REFRESH_TOKEN = "NOT_EXISTED";
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;
    private final static Integer MIN_6 = 3600;
    private final RefreshTokenGenerator refreshTokenGenerator = new UUIDRefreshTokenGenerator();
    private String EXISTED_REFRESH_TOKEN;
    private Integer EXISTED_USER_ID;

    @BeforeAll
    void setDataBeforeTest() {
        User user = User.builder()
                .banned(false)
                .email("test@gmail.com")
                .password("123")
                .nickname("nickname")
                .build();
        user.setRoles(Collections.singleton(Role.USER));
        User saved = this.userRepository.save(user);
        EXISTED_USER_ID = saved.getId();
        String token = this.refreshTokenGenerator.generate();
        RefreshToken refreshToken = RefreshToken.builder()
                .id(1)
                .refreshToken(token)
                .user(saved)
                .expireDate(Instant.now().plusSeconds(MIN_6))
                .build();
        RefreshToken savedToken = this.refreshTokenRepository.save(refreshToken);
        EXISTED_REFRESH_TOKEN = savedToken.getRefreshToken();
    }

    @Test
    @DisplayName("Existed token")
    void findExistedRefreshTokenByRefreshToken() {
        Optional<RefreshToken> token = this.refreshTokenRepository.findRefreshTokenByRefreshToken(EXISTED_REFRESH_TOKEN);
        assertNotNull(token);
    }

    @Test
    @DisplayName("Not existed token")
    void findNotExistedRefreshTokenByRefreshToken() {
        Optional<RefreshToken> token = this.refreshTokenRepository.findRefreshTokenByRefreshToken(NOT_EXISTED_REFRESH_TOKEN);
        assertThat(token).isEmpty();
    }

    @Test
    @DisplayName("Delete refresh token by existed user id")
    void deleteRefreshTokenByExistedUserId() {
        this.refreshTokenRepository.deleteRefreshTokenByUserId(EXISTED_USER_ID);
        assertEquals(0, this.refreshTokenRepository.findAll().size());
    }

    @Test
    @DisplayName("Delete refresh token by user id")
    void deleteRefreshTokenByNotExistedUserId() {
        this.refreshTokenRepository.deleteRefreshTokenByUserId(NOT_EXISTED_USER_ID);
        assertEquals(1, this.refreshTokenRepository.findAll().size());
    }

    @AfterAll
    void afterAll() {
        this.refreshTokenRepository.deleteAll();
        this.userRepository.deleteAll();
    }
}