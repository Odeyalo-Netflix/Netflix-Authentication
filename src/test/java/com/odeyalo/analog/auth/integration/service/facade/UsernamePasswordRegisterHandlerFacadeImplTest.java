package com.odeyalo.analog.auth.integration.service.facade;

import com.odeyalo.analog.auth.dto.response.JwtTokenResponseDTO;
import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.exceptions.EmailExistException;
import com.odeyalo.analog.auth.exceptions.NicknameExistException;
import com.odeyalo.analog.auth.integration.AbstractIntegrationTest;
import com.odeyalo.analog.auth.repository.RefreshTokenRepository;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.service.events.EventHandlerManager;
import com.odeyalo.analog.auth.service.facade.register.UsernamePasswordRegisterHandlerFacadeImpl;
import com.odeyalo.analog.auth.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.security.auth.message.AuthException;

import static org.junit.jupiter.api.Assertions.*;

class UsernamePasswordRegisterHandlerFacadeImplTest extends AbstractIntegrationTest {
    @MockBean
    EventHandlerManager eventManager;
    @Autowired
    private UsernamePasswordRegisterHandlerFacadeImpl registerHandlerFacade;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    private static final String NOT_EXISTED_USER_EMAIL = "notExisted@gmail.com";
    private static final String NOT_EXISTED_USER_NICKNAME = "notExistedNickname";
    private static final String USER_PASSWORD = "password";
    private static final String EXISTED_USER_EMAIL = "existed@gmail.com";
    private static final String EXISTED_USER_NICKNAME = "existedNickname";

    @BeforeEach
    void setUp() {
        User user = TestUtils.buildUser(EXISTED_USER_EMAIL, EXISTED_USER_NICKNAME, USER_PASSWORD, false, Role.USER);
        this.userRepository.save(user);
    }

    @Test
    @DisplayName("Register not existed user email")
    void registerNotExistedEmail() throws AuthException {
        User user = TestUtils.buildUser(NOT_EXISTED_USER_EMAIL, NOT_EXISTED_USER_NICKNAME, USER_PASSWORD, false, Role.USER);
        JwtTokenResponseDTO save = this.registerHandlerFacade.save(user);
        assertNotNull(save.getRefreshToken());
        assertNotNull(save.getRefreshToken());
        assertTrue(save.isSuccess());
        int size = this.userRepository.findAll().size();
        assertEquals(2, size);
    }
    @Test
    @DisplayName("Register existed  user email")
    void registerExistedUserEmail() {
        User user = TestUtils.buildUser(EXISTED_USER_EMAIL, NOT_EXISTED_USER_NICKNAME, USER_PASSWORD, false, Role.USER);
        assertThrows(EmailExistException.class, () -> this.registerHandlerFacade.save(user));
    }
    @Test
    @DisplayName("Register existed  user nickname")
    void registerExistedUserNickname() {
        User user = TestUtils.buildUser(NOT_EXISTED_USER_EMAIL, EXISTED_USER_NICKNAME, USER_PASSWORD, false, Role.USER);
        assertThrows(NicknameExistException.class, () -> this.registerHandlerFacade.save(user));
    }
    @AfterEach
    void clear() {
        this.refreshTokenRepository.deleteAll();
        this.userRepository.deleteAll();
    }
}
