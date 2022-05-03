package com.odeyalo.analog.auth.service.recovery;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.VerificationCode;
import com.odeyalo.analog.auth.exceptions.UserNotExistException;
import com.odeyalo.analog.auth.exceptions.IncorrectResetPasswordCodeException;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.service.facade.mail.VerificationCodeMailSenderFacade;
import com.odeyalo.analog.auth.service.support.PasswordRecoverySaverSupport;
import com.odeyalo.analog.auth.service.support.generatators.CodeGenerator;
import com.odeyalo.analog.auth.service.support.verification.CodeVerificationManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class EmailCodePasswordRecoveryManager implements PasswordRecoveryManager {
    private final PasswordRecoverySaverSupport passwordRecoverySaverSupport;
    private final UserRepository userRepository;
    private final VerificationCodeMailSenderFacade verificationCodeMailSenderFacade;
    private final CodeVerificationManager codeVerificationManager;

    public EmailCodePasswordRecoveryManager(
            @Qualifier("bcryptEncoderPasswordRecoverySaverSupport") PasswordRecoverySaverSupport passwordRecoverySaverSupport,
            UserRepository userRepository,
            @Qualifier("kafkaMessageBrokerVerificationCodeMailSenderFacade") VerificationCodeMailSenderFacade verificationCodeMailSenderFacade,
            @Qualifier("emailCodeVerificationManager") CodeVerificationManager codeVerificationManager) {
        this.passwordRecoverySaverSupport = passwordRecoverySaverSupport;
        this.userRepository = userRepository;
        this.verificationCodeMailSenderFacade = verificationCodeMailSenderFacade;
        this.codeVerificationManager = codeVerificationManager;
    }

    @Override
    public void sendResetPasswordCode(String email) {
        User user = this.userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotExistException("User not exist"));
        this.verificationCodeMailSenderFacade.generateAndSend(user, CodeGenerator.DEFAULT_CODE_LENGTH);
    }

    @Override
    public void changePassword(String code, String newPassword) {
        VerificationCode verificationCode = this.codeVerificationManager.getVerificationCodeByCodeValue(code).orElseThrow(
                    () -> new IncorrectResetPasswordCodeException("Presented code is wrong")
            );
        this.passwordRecoverySaverSupport.updatePassword(verificationCode.getUser(), newPassword);
    }

    @Override
    public boolean checkResetPasswordCode(String code) {
        return this.codeVerificationManager.verifyCode(code);
    }

    @Override
    public PasswordRecoveryType getPasswordRecoveryType() {
        return PasswordRecoveryType.EMAIL;
    }
}
