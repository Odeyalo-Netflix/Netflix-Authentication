package com.odeyalo.analog.auth.unit.service.recovery;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.VerificationCode;
import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.exceptions.IncorrectResetPasswordCodeException;
import com.odeyalo.analog.auth.exceptions.UserNotExistException;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.service.recovery.PhoneNumberSMSPasswordRecoveryManager;
import com.odeyalo.analog.auth.service.sender.sms.PhoneNumberMessageSender;
import com.odeyalo.analog.auth.service.support.BcryptEncoderPasswordRecoverySaverSupport;
import com.odeyalo.analog.auth.service.support.PasswordRecoverySaverSupport;
import com.odeyalo.analog.auth.service.support.generatators.CodeGenerator;
import com.odeyalo.analog.auth.service.support.verification.CodeVerificationManager;
import com.odeyalo.analog.auth.utils.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PhoneNumberSMSPasswordRecoveryManagerUnitTest {
    private PhoneNumberMessageSender phoneNumberMessageSender;
    private CodeVerificationManager codeVerificationManager;
    private PasswordRecoverySaverSupport passwordRecoverySaverSupport;
    private UserRepository userRepository;
    private PhoneNumberSMSPasswordRecoveryManager passwordRecoveryManager;

    private static final String USER_EMAIL = "email@gmail.com";
    private static final String USER_NICKNAME = "USER_NICKNAME";
    private static final String USER_PASSWORD = "password";
    private static final String EXISTED_USER_PHONE_NUMBER = "380238750254";
    private static final String NOT_EXISTED_USER_PHONE_NUMBER = "NOT_EXIST";
    private static final String EXISTED_CODE = "6666";
    private static final String NOT_EXISTED_CODE = "NOT EXISTED";
    private static final String NEW_USER_PASSWORD = "NEW PASSWORD";
    private final User user = TestUtils.buildUser(USER_EMAIL, USER_NICKNAME, USER_PASSWORD, false, EXISTED_USER_PHONE_NUMBER, Role.USER);

    private static final Integer VERIFICATION_CODE_ID = 1;
    private static final String PHONE_MESSAGE = "YOUR CODE IS: " + EXISTED_CODE;

    @BeforeAll
    void setUp() {
        this.mockCodeVerificationManager();
        this.mockPasswordRecoverySaverSupport();
        this.mockUserRepository();
        this.mockPhoneNumberMessageSender();
        this.passwordRecoveryManager = new PhoneNumberSMSPasswordRecoveryManager(phoneNumberMessageSender, codeVerificationManager, passwordRecoverySaverSupport, userRepository);
    }


    @Test
    @DisplayName("Send reset password code to existed phone number and expect no exceptions")
    void sendResetPasswordCodeToExistedPhoneNumber() {
        assertDoesNotThrow(() -> this.passwordRecoveryManager.sendResetPasswordCode(EXISTED_USER_PHONE_NUMBER));
    }
    @Test
    @DisplayName("Send reset password code to not existed phone number and expect exception")
    void sendResetPasswordCodeToNotExistedPhoneNumber() {
        assertThrows(UserNotExistException.class, () -> this.passwordRecoveryManager.sendResetPasswordCode(NOT_EXISTED_USER_PHONE_NUMBER));
    }

    @Test
    @DisplayName("Change password with correct code and expect no exceptions")
    void changePasswordWithCorrectCode() {
        assertDoesNotThrow(() -> this.passwordRecoveryManager.changePassword(EXISTED_CODE, NEW_USER_PASSWORD));
    }

    @Test
    @DisplayName("Change password with wrong code and expect exception")
    void changePasswordWithWrongCode() {
        assertThrows(IncorrectResetPasswordCodeException.class, () -> this.passwordRecoveryManager.changePassword(NOT_EXISTED_CODE, NEW_USER_PASSWORD));
    }

    @Test
    @DisplayName("Verify existed reset code and expect true ")
    void checkExistedResetPasswordCode() {
        assertTrue(this.passwordRecoveryManager.checkResetPasswordCode(EXISTED_CODE));
    }

    @Test
    @DisplayName("Verify not existed code and expect false ")
    void checkNotExistedResetPasswordCode() {
        assertFalse(this.passwordRecoveryManager.checkResetPasswordCode(NOT_EXISTED_CODE));
    }


    private void mockPhoneNumberMessageSender() {
        this.phoneNumberMessageSender = Mockito.mock(PhoneNumberMessageSender.class);
        doAnswer(invocation -> invocation).doNothing().when(phoneNumberMessageSender).sendMessage(EXISTED_USER_PHONE_NUMBER, PHONE_MESSAGE);
    }
    private void mockUserRepository() {
        this.userRepository = Mockito.mock(UserRepository.class);

        doAnswer((answer) -> {
            User user = (User) answer.getArguments()[0];
            user.setId(1);
            return user;
        }).when(userRepository).save(Mockito.any(User.class));

        when(userRepository.findUserByPhoneNumber(EXISTED_USER_PHONE_NUMBER)).thenReturn(Optional.of(user));

        when(userRepository.findUserByEmail(NOT_EXISTED_USER_PHONE_NUMBER)).thenReturn(Optional.empty());
    }

    private void mockPasswordRecoverySaverSupport() {
        this.passwordRecoverySaverSupport = Mockito.mock(BcryptEncoderPasswordRecoverySaverSupport.class);
        doAnswer(invocation -> invocation).when(passwordRecoverySaverSupport).updatePassword(USER_EMAIL, NEW_USER_PASSWORD);

        doAnswer(invocation -> invocation).when(passwordRecoverySaverSupport).updatePassword(user, NEW_USER_PASSWORD);
    }

    private void mockCodeVerificationManager() {
        this.codeVerificationManager = Mockito.mock(CodeVerificationManager.class);
        VerificationCode verificationCode = VerificationCode.builder()
                .id(VERIFICATION_CODE_ID)
                .user(user)
                .isActivated(false)
                .expired(LocalDateTime.now().plusMinutes(5))
                .build();
        when(codeVerificationManager.getVerificationCodeByCodeValue(EXISTED_CODE)).thenReturn(
                Optional.of(verificationCode));
        when(codeVerificationManager.getVerificationCodeByCodeValue(NOT_EXISTED_CODE)).thenReturn(Optional.empty());

        when(codeVerificationManager.verifyCode(EXISTED_CODE)).thenReturn(true);
        when(codeVerificationManager.verifyCode(NOT_EXISTED_CODE)).thenReturn(false);
        when(codeVerificationManager.generateAndSave(user, CodeGenerator.DEFAULT_CODE_LENGTH, CodeVerificationManager.DEFAULT_ACTIVE_MINUTES))
                .thenReturn(verificationCode);
    }
}
