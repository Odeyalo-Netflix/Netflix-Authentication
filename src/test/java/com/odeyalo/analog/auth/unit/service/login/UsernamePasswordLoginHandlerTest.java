package com.odeyalo.analog.auth.unit.service.login;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.exceptions.UserNotExistException;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.service.login.UsernamePasswordLoginHandler;
import com.odeyalo.analog.auth.utils.TestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UsernamePasswordLoginHandlerTest {
    @Autowired
    private UsernamePasswordLoginHandler loginHandler;
    @Autowired
    private UserRepository userRepository;

    private static final String EXISTED_USER_EMAIL = "existed@gmail.com";
    private static final String EXISTED_USER_NICKNAME = "existedNickname";
    private static final String RIGHT_USER_PASSWORD = "123";
    private static final String NOT_EXISTED_USER_EMAIL = "notExisted@gmail.com";
    private static final String NOT_EXISTED_USER_NICKNAME = "notExistedNickname";
    private static final String WRONG_USER_PASSWORD = "WRONG_PASSWORD";
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @BeforeAll
    void setUp() {
        User user = TestUtils.buildUser(EXISTED_USER_EMAIL, EXISTED_USER_NICKNAME, this.encoder.encode(RIGHT_USER_PASSWORD), false, Role.USER);
        this.userRepository.save(user);
    }
    @Test
    @DisplayName("Login with right nickname and password")
    void loginExistedUserWithRightNicknameAndPassword() {
        User userToLogin = User.builder().nickname(EXISTED_USER_NICKNAME).password(RIGHT_USER_PASSWORD).build();
        User user = this.loginHandler.login(userToLogin);
        assertNotNull(user);
        assertEquals(EXISTED_USER_EMAIL, user.getEmail());
        assertEquals(EXISTED_USER_NICKNAME, user.getNickname());
        assertEquals(Role.USER, user.getRoles().toArray()[0]);
        assertNotEquals(RIGHT_USER_PASSWORD, user.getPassword());
    }
    @Test
    @DisplayName("Login with right nickname and password")
    void loginExistedUserWithWrongNicknameAndPassword() {
        User userToLogin = User.builder().nickname(NOT_EXISTED_USER_NICKNAME).password(WRONG_USER_PASSWORD).build();
        assertThrows(UserNotExistException.class, () -> this.loginHandler.login(userToLogin));
    }
    @Test
    @DisplayName("Login with right nickname and password")
    void loginExistedUserWithWrongNicknameAndRightPassword() {
        User userToLogin = User.builder().nickname(NOT_EXISTED_USER_NICKNAME).password(RIGHT_USER_PASSWORD).build();
        assertThrows(UserNotExistException.class, () -> this.loginHandler.login(userToLogin));
    }
    @Test
    @DisplayName("Login with right nickname and wrong password")
    void loginExistedUserWithRightNicknameAndWrongPassword() {
        User userToLogin = User.builder().nickname(EXISTED_USER_NICKNAME).password(WRONG_USER_PASSWORD).build();
        assertThrows(UserNotExistException.class, () -> this.loginHandler.login(userToLogin));
    }

    @AfterAll
    void clear() {
        this.userRepository.deleteAll();
    }
}