package com.odeyalo.analog.auth.integration.service.recovery;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.VerificationCode;
import com.odeyalo.analog.auth.entity.enums.AuthProvider;
import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.integration.configs.EmailCodePasswordRecoveryManagerIntegrationTestConfiguration;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.repository.VerificationCodeRepository;
import com.odeyalo.analog.auth.service.recovery.EmailCodePasswordRecoveryManager;
import com.odeyalo.analog.auth.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = EmailCodePasswordRecoveryManagerIntegrationTestConfiguration.class)
class EmailCodePasswordRecoveryManagerIntegrationTest {
    @Autowired
    private EmailCodePasswordRecoveryManager manager;
    @Autowired
    private VerificationCodeRepository verificationCodeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final String EXISTED_USER_EMAIL = "email@gmail.com";
    private static final String USER_NICKNAME = "USER_NICKNAME";
    private static final String USER_PASSWORD = "password";
    private static final String EXISTED_CODE = "CODE_123";
    private static final String NOT_EXISTED_CODE = "NOT EXISTED";
    private static final String NEW_USER_PASSWORD = "NEW PASSWORD";
    private User user;

    @BeforeEach
    void setUp() {
        user = this.userRepository.save(TestUtils.buildUser(EXISTED_USER_EMAIL, USER_NICKNAME, USER_PASSWORD, false, AuthProvider.LOCAL, true, "", Role.USER));
    }

    @Test
    void sendResetPasswordCode() {
        assertDoesNotThrow(() -> this.manager.sendResetPasswordCode(EXISTED_USER_EMAIL));
        Optional<VerificationCode> optional = this.verificationCodeRepository.findVerificationCodeByUser_Email(EXISTED_USER_EMAIL);
        assertTrue(optional.isPresent());
        VerificationCode verificationCode = optional.get();
        assertEquals(user, verificationCode.getUser());
        assertNotNull(verificationCode.getCodeValue());
    }

    @Test
    void changeExistedCodePassword() {
        //given
        VerificationCode code = VerificationCode.builder().codeValue(EXISTED_CODE).user(user).isActivated(false).expired(LocalDateTime.now().plusMinutes(5)).build();
        this.verificationCodeRepository.save(code);

        //when
        this.manager.changePassword(EXISTED_CODE, NEW_USER_PASSWORD);

        // then
        User user = this.userRepository.findUserByEmail(EXISTED_USER_EMAIL).get();
        String actualPassword = user.getPassword();
        boolean matches = passwordEncoder.matches(NEW_USER_PASSWORD, user.getPassword());
        assertTrue(matches);
        assertNotEquals(USER_PASSWORD, actualPassword);
        assertNotEquals(NEW_USER_PASSWORD, actualPassword);
        assertNotEquals(this.user, user);
    }

    @Test
    void checkExistedResetPasswordCode() {
        VerificationCode code = VerificationCode.builder().codeValue(EXISTED_CODE).user(user).isActivated(false).expired(LocalDateTime.now().plusMinutes(5)).build();
        this.verificationCodeRepository.save(code);
        assertTrue(manager.checkResetPasswordCode(EXISTED_CODE));

    }
    @Test
    void checkNotExistedResetPasswordCode() {
        assertFalse(manager.checkResetPasswordCode(NOT_EXISTED_CODE));
    }

    @AfterEach
    void clear() {
        this.verificationCodeRepository.deleteAll();
        this.userRepository.deleteAll();
    }
}
