package com.odeyalo.analog.auth.unit.service.register;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.enums.AuthProvider;
import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.exceptions.EmailExistException;
import com.odeyalo.analog.auth.exceptions.NicknameExistException;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.service.register.UsernamePasswordRegisterHandler;
import com.odeyalo.analog.auth.service.support.UserEmailChecker;
import com.odeyalo.analog.auth.service.support.UserNicknameChecker;
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

import javax.security.auth.message.AuthException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UsernamePasswordRegisterHandlerUnitTest {
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    @Spy
    UserNicknameChecker userNicknameChecker = new UserNicknameChecker(userRepository);
    @Spy
    UserEmailChecker userEmailChecker = new UserEmailChecker(userRepository);
    @Spy
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    UsernamePasswordRegisterHandler usernamePasswordRegisterHandler;
    public static final String EXISTED_USER_EMAIL = "existed@gmail.com";
    public static final String EXISTED_USER_PASSWORD = "password";
    public static final String EXISTED_USER_NICKNAME = "nickname";
    public static final String NOT_EXISTED_USER_EMAIL = "not_existed@gmail.com";
    public static final String NOT_EXISTED_USER_PASSWORD = "wrong_password";
    public static final String NOT_EXISTED_USER_NICKNAME = "not_existed_nickname";
    public static final Integer GENERATED_USER_ID = 5;

    @BeforeAll
    void setup() {
      this.usernamePasswordRegisterHandler = new UsernamePasswordRegisterHandler(userRepository, userEmailChecker, userNicknameChecker, encoder);
    }

    @BeforeEach
    void beforeEach() {
        String encodedPassword = this.encoder.encode(EXISTED_USER_PASSWORD);
        User existedUser = TestUtils.buildUser(1, EXISTED_USER_EMAIL, EXISTED_USER_NICKNAME, encodedPassword, false, AuthProvider.LOCAL, "", Role.USER);
        doAnswer(mock -> {
            User user = (User) mock.getArguments()[0];
            user.setId(GENERATED_USER_ID);
            return user;
        }).when(userRepository).save(any(User.class));
        Mockito.when(this.userRepository.findUserByEmail(EXISTED_USER_EMAIL)).thenReturn(Optional.of(existedUser));
        Mockito.when(this.userRepository.findUserByEmail(NOT_EXISTED_USER_EMAIL)).thenReturn(Optional.empty());
        Mockito.when(this.userRepository.findUserByNickname(EXISTED_USER_NICKNAME)).thenReturn(Optional.of(existedUser));
        Mockito.when(this.userRepository.findUserByNickname(NOT_EXISTED_USER_NICKNAME)).thenReturn(Optional.empty());
    }

    @Test
    void registerExistedUserEmail_andExpectException() {
        User user = User.builder().nickname(NOT_EXISTED_USER_NICKNAME).email(EXISTED_USER_EMAIL).password(EXISTED_USER_PASSWORD).build();
        assertThrows(EmailExistException.class, () -> this.usernamePasswordRegisterHandler.register(user));
    }
    @Test
    void registerExistedUserNickname_andExpectException() {
        User user = User.builder().nickname(EXISTED_USER_NICKNAME).email(NOT_EXISTED_USER_EMAIL).password(EXISTED_USER_PASSWORD).build();
        assertThrows(NicknameExistException.class, () -> this.usernamePasswordRegisterHandler.register(user));
    }
    @Test
    void registerNotExistedUser_andExpectUser() throws AuthException {
        User user = User.builder().nickname(NOT_EXISTED_USER_NICKNAME).email(NOT_EXISTED_USER_EMAIL).password(EXISTED_USER_PASSWORD).build();
        User register = this.usernamePasswordRegisterHandler.register(user);
        assertNotNull(register);
        assertEquals(NOT_EXISTED_USER_EMAIL, register.getEmail());
        assertEquals(NOT_EXISTED_USER_NICKNAME, register.getNickname());
        assertNotNull(register.getImage());
        assertEquals(AuthProvider.LOCAL, register.getAuthProvider());
        assertNotNull(register.getPassword());
        assertEquals(1, register.getRoles().size());
        Set<Role> roles = new HashSet<>(1);
        roles.add(Role.USER);
        assertEquals(roles, register.getRoles());
        assertEquals(GENERATED_USER_ID, register.getId());
    }

    @Test
    void getAuthProvider() {
        AuthProvider authProvider = this.usernamePasswordRegisterHandler.getAuthProvider();
        assertEquals(AuthProvider.LOCAL, authProvider);
    }
}
