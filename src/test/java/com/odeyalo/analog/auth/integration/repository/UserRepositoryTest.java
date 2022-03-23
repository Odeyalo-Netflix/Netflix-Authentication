package com.odeyalo.analog.auth.integration.repository;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.utils.TestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    private static final String EXISTED_USER_EMAIL = "user@gmail.com";
    private static final String EXISTED_USER_PASSWORD = "password1";
    private static final String EXISTED_ADMIN_NICKNAME = "admin";
    private static final String EXISTED_ADMIN_PASSWORD = "password2";

    @BeforeAll
    void setUp() {
        List<User> users = new ArrayList<>(3);
        users.add(TestUtils.buildUser("user@gmail.com", "user", "password1", false, Role.USER));
        users.add(TestUtils.buildUser("admin@gmail.com", "admin", "password2", false, Role.ADMIN));
        users.add(TestUtils.buildUser("moderator@gmail.com", "moderator", "password3", false, Role.MODERATOR));
        this.userRepository.saveAll(users);
    }

    @Test
    @DisplayName("find existed user by email and password")
    void findExistedUserByEmailAndPassword() {
        Optional<User> userOptional = this.userRepository.findUserByEmailAndPassword(EXISTED_USER_EMAIL, EXISTED_USER_PASSWORD);
        assertThat(userOptional).isNotEmpty();
        User user = userOptional.get();
        assertEquals("user@gmail.com", user.getEmail());
        assertEquals("user", user.getNickname());
        assertEquals("password1", user.getPassword());
        assertEquals(Role.USER, user.getRoles().toArray()[0]);
        assertFalse(user.isUserBanned());
    }

    @Test
    @DisplayName("find existed user by nickname and password")
    void findUserByNicknameAndPassword() {
        Optional<User> userOptional = this.userRepository.findUserByNicknameAndPassword(EXISTED_ADMIN_NICKNAME, EXISTED_ADMIN_PASSWORD);
        assertThat(userOptional).isNotEmpty();
        User user = userOptional.get();
        assertEquals("admin@gmail.com", user.getEmail());
        assertEquals("admin", user.getNickname());
        assertEquals("password2", user.getPassword());
        assertEquals(Role.ADMIN, user.getRoles().toArray()[0]);
        assertFalse(user.isUserBanned());
    }

    @Test
    @DisplayName("Find by nickname")
    void findUserByNickname() {
        Optional<User> userOptional = this.userRepository.findUserByNickname(EXISTED_ADMIN_NICKNAME);
        assertThat(userOptional).isNotEmpty();
        User user = userOptional.get();
        assertEquals("admin@gmail.com", user.getEmail());
        assertEquals("admin", user.getNickname());
        assertEquals("password2", user.getPassword());
        assertEquals(Role.ADMIN, user.getRoles().toArray()[0]);
        assertFalse(user.isUserBanned());
    }

    @Test
    void findUserByEmail() {
        Optional<User> userOptional = this.userRepository.findUserByEmail(EXISTED_USER_EMAIL);
        assertThat(userOptional).isNotEmpty();
        User user = userOptional.get();
        assertEquals("user@gmail.com", user.getEmail());
        assertEquals("user", user.getNickname());
        assertEquals("password1", user.getPassword());
        assertEquals(Role.USER, user.getRoles().toArray()[0]);
        assertFalse(user.isUserBanned());
    }

    @AfterAll
    void clear() {
        this.userRepository.deleteAll();
    }



}
