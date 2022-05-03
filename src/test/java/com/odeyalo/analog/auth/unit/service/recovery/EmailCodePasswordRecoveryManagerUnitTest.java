package com.odeyalo.analog.auth.unit.service.recovery;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.VerificationCode;
import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.exceptions.IncorrectResetPasswordCodeException;
import com.odeyalo.analog.auth.exceptions.UserNotExistException;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.service.facade.mail.VerificationCodeMailSenderFacade;
import com.odeyalo.analog.auth.service.recovery.EmailCodePasswordRecoveryManager;
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
class EmailCodePasswordRecoveryManagerUnitTest {
    private UserRepository userRepository;
    private CodeVerificationManager codeVerificationManager;
    private PasswordRecoverySaverSupport passwordRecoverySaverSupport;
    private VerificationCodeMailSenderFacade verificationCodeMailSenderFacade;
    private EmailCodePasswordRecoveryManager manager;

    private static final String USER_EMAIL = "email@gmail.com";
    private static final String NOT_EXISTED_USER_EMAIL = "not_existed@gmail.com";
    private static final String USER_NICKNAME = "USER_NICKNAME";
    private static final String USER_PASSWORD = "password";
    private static final String EXISTED_CODE = "CODE_123";
    private static final String NOT_EXISTED_CODE = "NOT EXISTED";
    private static final String NEW_USER_PASSWORD = "NEW PASSWORD";
    private final User user = TestUtils.buildUser(USER_EMAIL, USER_NICKNAME, USER_PASSWORD, false, Role.USER);

    private static final Integer VERIFICATION_CODE_ID = 1;

    @BeforeAll
    void setup() {
        this.mockPasswordRecoverySaverSupport();
        this.mockCodeVerificationManager();
        this.mockVerificationCodeMailSenderFacade();
        this.mockUserRepository();
        this.manager = new EmailCodePasswordRecoveryManager(passwordRecoverySaverSupport, userRepository, verificationCodeMailSenderFacade, codeVerificationManager);
    }
    @Test
    @DisplayName("Send reset password code for existed user")
    void sendResetPasswordCodeForExistedUser() {
        assertDoesNotThrow(() -> this.manager.sendResetPasswordCode(USER_EMAIL));
    }

    @Test
    @DisplayName("Send reset password code for not existed user")
    void sendResetPasswordCodeForNotExistedUser() {
        assertThrows(UserNotExistException.class, () -> this.manager.sendResetPasswordCode(NOT_EXISTED_USER_EMAIL));
    }

    @Test
    @DisplayName("Change password with correct code and expect not exceptions")
    void changePasswordWithCorrectCode() {
        assertDoesNotThrow(() -> this.manager.changePassword(EXISTED_CODE, NEW_USER_PASSWORD));
    }

    @Test
    @DisplayName("Change password with wrong code and expect exception")
    void changePasswordWithWrongCode() {
        assertThrows(IncorrectResetPasswordCodeException.class, () -> this.manager.changePassword(NOT_EXISTED_CODE, NEW_USER_PASSWORD));
    }

    @Test
    @DisplayName("Check existed reset password code and expect true")
    void checkExistedResetPasswordCode() {
        assertTrue(this.manager.checkResetPasswordCode(EXISTED_CODE));
    }
    @Test
    @DisplayName("Check not existed reset password code and expect true")
    void checkNotExistedResetPasswordCode() {
        assertFalse(this.manager.checkResetPasswordCode(NOT_EXISTED_CODE));
    }



    private void mockUserRepository() {
        this.userRepository = Mockito.mock(UserRepository.class);

        doAnswer((answer) -> {
            User user = (User) answer.getArguments()[0];
            user.setId(1);
            return user;
        }).when(userRepository).save(Mockito.any(User.class));

        when(userRepository.findUserByEmail(NOT_EXISTED_USER_EMAIL)).thenReturn(Optional.empty());

        when(userRepository.findUserByEmail(USER_EMAIL)).thenReturn(Optional.of(user));

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

    private void mockVerificationCodeMailSenderFacade() {
        this.verificationCodeMailSenderFacade = Mockito.mock(VerificationCodeMailSenderFacade.class);
        doAnswer(invocationOnMock -> invocationOnMock).when(verificationCodeMailSenderFacade).generateAndSend(user, CodeGenerator.DEFAULT_CODE_LENGTH);
    }

    private void mockPasswordRecoverySaverSupport() {
        this.passwordRecoverySaverSupport = Mockito.mock(BcryptEncoderPasswordRecoverySaverSupport.class);
        doAnswer(invocation -> invocation).when(passwordRecoverySaverSupport).updatePassword(USER_EMAIL, NEW_USER_PASSWORD);

        doAnswer(invocation -> invocation).when(passwordRecoverySaverSupport).updatePassword(user, NEW_USER_PASSWORD);
    }
}
