package com.odeyalo.analog.auth.unit.service.facade;

import com.odeyalo.analog.auth.dto.response.RefreshTokenResponseDTO;
import com.odeyalo.analog.auth.entity.RefreshToken;
import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.exceptions.RefreshTokenException;
import com.odeyalo.analog.auth.repository.RefreshTokenRepository;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.service.facade.JwtWithRefreshTokenResponseDTOBuilderFacadeImpl;
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
class JwtWithRefreshTokenResponseDTOBuilderFacadeImplTest {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtWithRefreshTokenResponseDTOBuilderFacadeImpl builderFacade;

    private final RefreshTokenGenerator generator = new UUIDRefreshTokenGenerator();
    private String existedRefreshToken;
    private static final String NOT_EXISTED_REFRESH_TOKEN = "-1";
    private static final String USER_EMAIL = "email@gmail.com";
    private static final String USER_NICKNAME = "nickname";
    private static final String USER_PASSWORD = "password";


    @BeforeAll
    void setUp() {
        User user = this.userRepository.save(TestUtils.buildUser(USER_EMAIL, USER_NICKNAME, USER_PASSWORD, false, Role.USER));
        this.existedRefreshToken =  this.generator.generate();
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(this.existedRefreshToken)
                .user(user)
                .expireDate(Instant.now().plusSeconds(10))
                .build();
        this.refreshTokenRepository.save(refreshToken);
    }

    @Test
    @DisplayName("generate by existed refresh token")
    void generateResponseDTByExistedToken() {
        RefreshTokenResponseDTO dto = this.builderFacade.generateResponseDTO(this.existedRefreshToken);
        assertNotNull(dto);
        assertNotNull(dto.getJwtToken());
        assertNotNull(dto.getRefreshToken());
    }

    @Test
    @DisplayName("generate by not existed refresh token")
    void generateResponseDTByNotExistedToken() {
        assertThrows(RefreshTokenException.class, () -> this.builderFacade.generateResponseDTO(NOT_EXISTED_REFRESH_TOKEN));
    }

    @AfterAll
    void clear() {
        this.refreshTokenRepository.deleteAll();
        this.userRepository.deleteAll();
    }
}