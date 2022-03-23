package com.odeyalo.analog.auth.unit.service.login;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.enums.AuthProvider;
import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.exceptions.UserNotExistException;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.service.login.UsernamePasswordLoginHandler;
import com.odeyalo.analog.auth.utils.TestUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UsernamePasswordLoginHandlerUnitTest {
    @Mock
    UserRepository userRepository;
    @Spy
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    @InjectMocks
    UsernamePasswordLoginHandler usernamePasswordLoginHandler;

    private static final Integer USER_ID = 1;
    private static final String CORRECT_USER_EMAIL = "correct@gmail.com";
    private static final String CORRECT_USER_NICKNAME = "NICKNAME";
    private static final String CORRECT_USER_PASSWORD = "password";
    private static final String WRONG_USER_NICKNAME = "WRONG_NICK";
    private static final String WRONG_USER_EMAIL = "wrong@gmail.com";
    private static final String WRONG_USER_PASSWORD = "wrong password";

    @BeforeEach
    void setUp() {
        String encode = this.encoder.encode(CORRECT_USER_PASSWORD);
        User user = TestUtils.buildUser(USER_ID, CORRECT_USER_EMAIL, CORRECT_USER_NICKNAME, encode, false, AuthProvider.LOCAL, "", Role.USER);
        Mockito.when(this.userRepository.findUserByNickname(WRONG_USER_NICKNAME)).thenReturn(Optional.empty());
        Mockito.when(this.userRepository.findUserByNickname(CORRECT_USER_NICKNAME)).thenReturn(Optional.of(user));
    }

    @Test
    @DisplayName("Login with correct nickname and password")
    void loginExistedUserWithRightNicknameAndPassword() {
        User user = User.builder()
                .nickname(CORRECT_USER_NICKNAME)
                .password(CORRECT_USER_PASSWORD)
                .build();
        User loginUser = this.usernamePasswordLoginHandler.login(user);
        assertEquals(CORRECT_USER_EMAIL, loginUser.getEmail());
        assertEquals(CORRECT_USER_NICKNAME, loginUser.getNickname());
        assertEquals(1, loginUser.getId());
        assertEquals(AuthProvider.LOCAL, loginUser.getAuthProvider());
        assertEquals("", loginUser.getImage());
        assertEquals(1, loginUser.getRoles().size());
        assertEquals(Role.USER, new ArrayList<>(loginUser.getRoles()).get(0));
    }

    @Test
    @DisplayName("Login with wrong nickname and password")
    void loginExistedUserWithWrongNicknameAndPassword() {
        User user = User.builder()
                .nickname(WRONG_USER_NICKNAME)
                .password(WRONG_USER_PASSWORD)
                .build();
        assertThrows(UserNotExistException.class, () -> this.usernamePasswordLoginHandler.login(user));
    }

    @Test
    @DisplayName("Login with wrong nickname and correct password")
    void loginExistedUserWithWrongNicknameAndCorrectPassword() {
        User user = User.builder().nickname(WRONG_USER_NICKNAME).password(CORRECT_USER_PASSWORD).build();
        assertThrows(UserNotExistException.class, () -> this.usernamePasswordLoginHandler.login(user));
    }

    @Test
    @DisplayName("Login with correct nickname and wrong password")
    void loginExistedUserWithCorrectNicknameAndWrongPassword() {
        User user = User.builder().nickname(CORRECT_USER_NICKNAME).password(WRONG_USER_PASSWORD).build();
        assertThrows(UserNotExistException.class, () -> this.usernamePasswordLoginHandler.login(user));
    }
}
