package com.odeyalo.analog.auth.integration.service.support;

import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.service.support.UserNicknameChecker;
import com.odeyalo.analog.auth.utils.TestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class UserNicknameCheckerTest {
    @Autowired
    private UserNicknameChecker nicknameChecker;
    @Autowired
    private UserRepository userRepository;
    private static final String EXISTED_NICKNAME = "EXISTED";
    private static final String NOT_EXISTED_NICKNAME = "NOT_EXISTED";

    @BeforeAll
    void setUp() {
        this.userRepository.save(TestUtils.buildUser("email@gmail.com", EXISTED_NICKNAME, "123", false, Role.USER));
    }

    @Test
    @DisplayName("Check existed nickname and expect true")
    void checkExistedNickname() {
        boolean result = this.nicknameChecker.check(EXISTED_NICKNAME);
        assertTrue(result);
    }

    @Test
    @DisplayName("Check not existed nickname and expect false")
    void checkNotExistedNickname() {
        boolean result = this.nicknameChecker.check(NOT_EXISTED_NICKNAME);
        assertFalse(result);
    }


    @AfterAll
    void clear() {
        this.userRepository.deleteAll();
    }
}
