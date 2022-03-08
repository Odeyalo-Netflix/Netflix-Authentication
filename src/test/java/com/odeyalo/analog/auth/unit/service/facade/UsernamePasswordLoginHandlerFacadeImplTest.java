package com.odeyalo.analog.auth.unit.service.facade;

import com.odeyalo.analog.auth.dto.response.JwtTokenResponseDTO;
import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.exceptions.UserNotExistException;
import com.odeyalo.analog.auth.repository.RefreshTokenRepository;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.service.facade.UsernamePasswordLoginHandlerFacadeImpl;
import com.odeyalo.analog.auth.utils.TestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UsernamePasswordLoginHandlerFacadeImplTest {
    @Autowired
    private UsernamePasswordLoginHandlerFacadeImpl loginHandlerFacade;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private PasswordEncoder encoder;

    private static final String NOT_EXISTED_USER_EMAIL = "notExisted@gmail.com";
    private static final String NOT_EXISTED_USER_NICKNAME = "notExistedNickname";
    private static final String EXISTED_USER_EMAIL = "existed@gmail.com";
    private static final String EXISTED_USER_NICKNAME = "existedNickname";
    private static final String USER_PASSWORD = "password";

    @BeforeEach
    void setUp() {
        User user = TestUtils.buildUser(EXISTED_USER_EMAIL, EXISTED_USER_NICKNAME, encoder.encode(USER_PASSWORD), false, Role.USER);
        this.userRepository.save(user);
    }

    @Test
    @DisplayName("Login existed user with right nickname and password")
    void loginExistedUserWithRightNicknameAndPassword() {
        User user = TestUtils.buildUser(EXISTED_USER_EMAIL, EXISTED_USER_NICKNAME, USER_PASSWORD, false, Role.USER);
        JwtTokenResponseDTO dto = this.loginHandlerFacade.login(user);
        assertNotNull(dto.getRefreshToken());
        assertNotNull(dto.getJwtToken());
        assertTrue(dto.isSuccess());
        int size = this.refreshTokenRepository.findAll().size();
        assertEquals(1, size);
    }

    @Test
    @DisplayName("Login not existed user")
    void loginNotExistedUser() {
        User user = TestUtils.buildUser(NOT_EXISTED_USER_EMAIL, NOT_EXISTED_USER_NICKNAME, USER_PASSWORD, false, Role.USER);
        assertThrows(UserNotExistException.class, () -> this.loginHandlerFacade.login(user));
    }

    @AfterEach
    void clear() {
        this.refreshTokenRepository.deleteAll();
        this.userRepository.deleteAll();
    }
}