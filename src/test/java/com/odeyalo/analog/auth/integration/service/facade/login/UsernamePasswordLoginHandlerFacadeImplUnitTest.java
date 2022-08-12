package com.odeyalo.analog.auth.integration.service.facade.login;

import com.odeyalo.analog.auth.config.security.jwt.utils.SecretKeyJwtTokenProvider;
import com.odeyalo.analog.auth.dto.response.JwtTokenResponseDTO;
import com.odeyalo.analog.auth.entity.RefreshToken;
import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.enums.AuthProvider;
import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.exceptions.UserNotExistException;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.service.events.EventHandlerManager;
import com.odeyalo.analog.auth.service.facade.login.UsernamePasswordLoginHandlerFacadeImpl;
import com.odeyalo.analog.auth.service.login.UsernamePasswordLoginHandler;
import com.odeyalo.analog.auth.service.refresh.RefreshTokenProvider;
import com.odeyalo.analog.auth.service.support.CustomUserDetails;
import com.odeyalo.analog.auth.utils.TestUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class UsernamePasswordLoginHandlerFacadeImplUnitTest {
    UserRepository userRepository;
    EventHandlerManager eventHandlerManager;
    @Spy
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    SecretKeyJwtTokenProvider secretKeyJwtTokenProvider;
    RefreshTokenProvider refreshTokenProvider;
    UsernamePasswordLoginHandler loginHandler;
    UsernamePasswordLoginHandlerFacadeImpl loginHandlerFacade;

    private static final Integer USER_ID = 1;
    private static final String CORRECT_USER_EMAIL = "correct@gmail.com";
    private static final String CORRECT_USER_NICKNAME = "NICKNAME";
    private static final String CORRECT_USER_PASSWORD = "password";
    private static final String WRONG_USER_NICKNAME = "WRONG_NICK";
    private static final String WRONG_USER_PASSWORD = "wrong password";

    private static final String JWT_TOKEN_TEXT_VALUE = "JWT TOKEN VALUE";
    private static final String REFRESH_TOKEN_TEXT_VALUE = "REFRESH TOKEN VALUE";
    private static final String JWT_TOKEN_TEST_SECRET_KEY = "SECRET";
    private static final Integer JWT_TOKEN_TEST_EXPIRY_TIME = 600;

    @BeforeAll
    void beforeAll() {
        this.userRepository = Mockito.mock(UserRepository.class);
        this.refreshTokenProvider = Mockito.mock(RefreshTokenProvider.class);
        this.secretKeyJwtTokenProvider = Mockito.mock(SecretKeyJwtTokenProvider.class);
        this.loginHandler = new UsernamePasswordLoginHandler(userRepository, passwordEncoder);
        this.eventHandlerManager = Mockito.mock(EventHandlerManager.class);
        this.loginHandlerFacade = new UsernamePasswordLoginHandlerFacadeImpl(
                loginHandler, secretKeyJwtTokenProvider, refreshTokenProvider,
                eventHandlerManager);
        ReflectionTestUtils.setField(secretKeyJwtTokenProvider, "signingKey", JWT_TOKEN_TEST_SECRET_KEY.getBytes(), byte[].class);
        ReflectionTestUtils.setField(secretKeyJwtTokenProvider, "JWT_TOKEN_EXPIRATION_TIME", JWT_TOKEN_TEST_EXPIRY_TIME, Integer.class);
    }

    @BeforeEach
    void setUp() {
        String encode = this.passwordEncoder.encode(CORRECT_USER_PASSWORD);
        User user = TestUtils.buildUser(USER_ID, CORRECT_USER_EMAIL, CORRECT_USER_NICKNAME, encode, false, AuthProvider.LOCAL, true, "", Role.USER);
        Mockito.when(secretKeyJwtTokenProvider.generateJwtToken(any(CustomUserDetails.class))).thenReturn(JWT_TOKEN_TEXT_VALUE);
        Mockito.when(refreshTokenProvider.createAndSaveToken(user)).thenReturn(RefreshToken.builder().refreshToken(REFRESH_TOKEN_TEXT_VALUE).user(user).id(1).build());
        Mockito.when(userRepository.findUserByNickname(WRONG_USER_NICKNAME)).thenReturn(Optional.empty());
        Mockito.when(userRepository.findUserByNickname(CORRECT_USER_NICKNAME)).thenReturn(Optional.of(user));
    }

    @Test
    @DisplayName("Login existed activated user with correct data")
    void loginExistedUserWithCorrectNicknameAndPassword() {
        User dto = User.builder().nickname(CORRECT_USER_NICKNAME).password(CORRECT_USER_PASSWORD).build();
        JwtTokenResponseDTO tokenResponseDTO = this.loginHandlerFacade.login(dto);
        assertTrue(tokenResponseDTO.isSuccess());
        assertEquals(JWT_TOKEN_TEXT_VALUE, tokenResponseDTO.getJwtToken());
        assertEquals(REFRESH_TOKEN_TEXT_VALUE, tokenResponseDTO.getRefreshToken());
    }

    @Test
    @DisplayName("Login existed user with wrong password")
    void loginExistedUserWithCorrectNicknameAndWrongPassword() {
        User dto = User.builder().nickname(CORRECT_USER_NICKNAME).password(WRONG_USER_PASSWORD).build();
        assertThrows(UserNotExistException.class, () -> this.loginHandlerFacade.login(dto));
    }

    @Test
    @DisplayName("Login user with wrong nickname and password")
    void loginUserWithWrongNicknameAndWrongPassword() {
        User dto = User.builder().nickname(WRONG_USER_NICKNAME).password(WRONG_USER_PASSWORD).build();
        assertThrows(UserNotExistException.class, () -> this.loginHandlerFacade.login(dto));
    }

    @Test
    @DisplayName("Login  user with wrong nickname and correct password")
    void loginExistedUserWithWrongNicknameAndCorrectPassword() {
        User dto = User.builder().nickname(WRONG_USER_NICKNAME).password(WRONG_USER_PASSWORD).build();
        assertThrows(UserNotExistException.class, () -> this.loginHandlerFacade.login(dto));
    }
}
