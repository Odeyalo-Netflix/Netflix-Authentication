package com.odeyalo.analog.auth.integration.service.register;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.exceptions.EmailExistException;
import com.odeyalo.analog.auth.exceptions.NicknameExistException;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.service.register.UsernamePasswordRegisterHandler;
import com.odeyalo.analog.auth.utils.TestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UsernamePasswordRegisterHandlerTest {

    @Autowired
    private UsernamePasswordRegisterHandler registerHandler;
    @Autowired
    private UserRepository userRepository;

    private static final String EXISTED_USER_EMAIL = "existed@gmail.com";
    private static final String EXISTED_USER_NICKNAME = "existedNickname";
    private static final String NOT_EXISTED_USER_EMAIL = "notExisted@gmail.com";
    private static final String NOT_EXISTED_USER_NICKNAME = "notExistedNickname";

    @BeforeEach
    void setUp() {
        this.userRepository.save(TestUtils.buildUser(EXISTED_USER_EMAIL, EXISTED_USER_NICKNAME, "123", false, Role.USER));
    }

    @Test
    @DisplayName("Register not existed user")
    void registerNotExistedUser() throws Exception {
        User user = TestUtils.buildUser(NOT_EXISTED_USER_EMAIL, NOT_EXISTED_USER_NICKNAME, "password1", false, Role.USER);
        this.registerHandler.register(user);
        int size = this.userRepository.findAll().size();
        assertEquals(2, size);
    }
    @Test
    @DisplayName("Register existed user email and expect exception")
    void registerExistedUserEmail() throws Exception {
        User user = TestUtils.buildUser(EXISTED_USER_EMAIL, NOT_EXISTED_USER_NICKNAME, "password1", false, Role.USER);
        assertThrows(EmailExistException.class, () -> this.registerHandler.register(user));
    }
    @Test
    @DisplayName("Register existed user nickname and expect exception")
    void registerExistedUserNickname() throws Exception {
        User user = TestUtils.buildUser(NOT_EXISTED_USER_EMAIL, EXISTED_USER_NICKNAME, "password1", false, Role.USER);
        assertThrows(NicknameExistException.class, () -> this.registerHandler.register(user));
    }

    @AfterEach
    void clear() {
        this.userRepository.deleteAll();
    }

}
