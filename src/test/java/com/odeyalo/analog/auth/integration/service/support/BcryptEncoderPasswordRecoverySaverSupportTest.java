package com.odeyalo.analog.auth.integration.service.support;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.enums.AuthProvider;
import com.odeyalo.analog.auth.integration.configs.BcryptEncoderPasswordRecoverySaverSupportTestConfiguration;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.service.support.BcryptEncoderPasswordRecoverySaverSupport;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = BcryptEncoderPasswordRecoverySaverSupportTestConfiguration.class)
@ExtendWith(SpringExtension.class)
class BcryptEncoderPasswordRecoverySaverSupportTest {
    @Autowired
    private BcryptEncoderPasswordRecoverySaverSupport bcryptEncoderPasswordRecoverySaverSupport;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;

    private final static String USER_NICKNAME = "nickname123";
    private final static String USER_EMAIL  = "email@gmail.com";
    private final static String NEW_USER_PASSWORD = "new_password";
    private final static String OLD_USER_PASSWORD = "password";

    @BeforeEach
    void setup() {
        System.out.println(bcryptEncoderPasswordRecoverySaverSupport);
        System.out.println(userRepository);
        String encodedPassword = encoder.encode(OLD_USER_PASSWORD);
        this.userRepository.save(
                User.builder().nickname(USER_NICKNAME)
                        .email(USER_EMAIL)
                        .password(encodedPassword)
                        .banned(false)
                        .authProvider(AuthProvider.LOCAL)
                        .phoneNumber("")
                        .image("")
                        .build()
        );
    }

    @Test
    void updatePassword() {
        // given
        Optional<User> userByEmail = this.userRepository.findUserByEmail(USER_EMAIL);
        assertTrue(userByEmail.isPresent());
        User user = userByEmail.get();

        this.bcryptEncoderPasswordRecoverySaverSupport.updatePassword(user, NEW_USER_PASSWORD);

        Optional<User> newUserOptional = this.userRepository.findUserByEmail(USER_EMAIL);
        assertTrue(newUserOptional.isPresent());
        User newUser = newUserOptional.get();
        // then
        assertNotEquals(NEW_USER_PASSWORD, newUser.getPassword());
        assertNotEquals(user, newUser);
    }

    @Test
    void testUpdatePassword() {
        Optional<User> userByEmail = this.userRepository.findUserByEmail(USER_EMAIL);
        assertTrue(userByEmail.isPresent());
        User user = userByEmail.get();

        this.bcryptEncoderPasswordRecoverySaverSupport.updatePassword(USER_EMAIL, NEW_USER_PASSWORD);

        Optional<User> newUserOptional = this.userRepository.findUserByEmail(USER_EMAIL);
        assertTrue(newUserOptional.isPresent());
        User newUser = newUserOptional.get();
        assertNotEquals(NEW_USER_PASSWORD, newUser.getPassword());
        assertNotEquals(user, newUser);
        assertNotEquals(user.getPassword(), newUser.getPassword());
    }

    @AfterEach
    void clear() {
        this.userRepository.deleteAll();
    }
}
