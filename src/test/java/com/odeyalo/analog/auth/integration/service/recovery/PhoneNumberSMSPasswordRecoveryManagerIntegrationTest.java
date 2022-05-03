package com.odeyalo.analog.auth.integration.service.recovery;

import com.odeyalo.analog.auth.integration.configs.EmailCodePasswordRecoveryManagerIntegrationTestConfiguration;
import com.odeyalo.analog.auth.integration.configs.PhoneNumberSMSPasswordRecoveryManagerIntegrationTestConfiguration;
import com.odeyalo.analog.auth.service.recovery.PhoneNumberSMSPasswordRecoveryManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PhoneNumberSMSPasswordRecoveryManagerIntegrationTestConfiguration.class)
class PhoneNumberSMSPasswordRecoveryManagerIntegrationTest {
    @Autowired
    private PhoneNumberSMSPasswordRecoveryManager passwordRecoveryManager;

    @Test
    void sendResetPasswordCode() {
//        passwordRecoveryManager.sendResetPasswordCode();
    }

    @Test
    void changePassword() {
    }

    @Test
    void checkResetPasswordCode() {
    }
}
