package com.odeyalo.analog.auth.service.recovery;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.VerificationCode;
import com.odeyalo.analog.auth.exceptions.IncorrectResetPasswordCodeException;
import com.odeyalo.analog.auth.exceptions.UserNotExistException;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.service.sender.sms.PhoneNumberMessageSender;
import com.odeyalo.analog.auth.service.support.PasswordRecoverySaverSupport;
import com.odeyalo.analog.auth.service.support.generatators.CodeGenerator;
import com.odeyalo.analog.auth.service.support.verification.CodeVerificationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PhoneNumberSMSPasswordRecoveryManager implements PasswordRecoveryManager {
    private final PhoneNumberMessageSender messageSender;
    private final CodeVerificationManager codeVerificationManager;
    private final PasswordRecoverySaverSupport passwordRecoverySaverSupport;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(PhoneNumberSMSPasswordRecoveryManager.class);

    public PhoneNumberSMSPasswordRecoveryManager(
            @Qualifier("kafkaMessageBrokerMicroserviceDelegatePhoneNumberMessageSender") PhoneNumberMessageSender messageSender,
            @Qualifier("phoneNumberCodeVerificationManager") CodeVerificationManager codeVerificationManager,
            @Qualifier("bcryptEncoderPasswordRecoverySaverSupport") PasswordRecoverySaverSupport passwordRecoverySaverSupport,
            UserRepository userRepository) {
        this.messageSender = messageSender;
        this.codeVerificationManager = codeVerificationManager;
        this.passwordRecoverySaverSupport = passwordRecoverySaverSupport;
        this.userRepository = userRepository;
    }

    @Override
    public void sendResetPasswordCode(String phoneNumber) {
        User user = this.userRepository.findUserByPhoneNumber(phoneNumber).orElseThrow(() -> new UserNotExistException("Presented phone number not existed"));
        VerificationCode verificationCode = this.codeVerificationManager.generateAndSave(user, CodeGenerator.DEFAULT_CODE_LENGTH, CodeVerificationManager.DEFAULT_ACTIVE_MINUTES);
        this.messageSender.sendMessage(phoneNumber, "Your code number is: " + verificationCode.getCodeValue());
        this.logger.info("Successfully sent message to kafka broker, generated verification code was: {}, user: {}", verificationCode.getCodeValue(), user);
    }

    @Override
    public void changePassword(String code, String newPassword) {
        Optional<VerificationCode> optional = this.codeVerificationManager.getVerificationCodeByCodeValue(code);
        if (!optional.isPresent()) {
            this.logger.info("Verification code is wrong: {}", code);
            throw new IncorrectResetPasswordCodeException("Recovery password code is wrong");
        }
        VerificationCode verificationCode = optional.get();
        User user = verificationCode.getUser();
        this.passwordRecoverySaverSupport.updatePassword(user, newPassword);
        this.logger.info("Successfully updated password for user: {}", user);
    }

    @Override
    public boolean checkResetPasswordCode(String code) {
        return codeVerificationManager.verifyCode(code);
    }

    @Override
    public PasswordRecoveryType getPasswordRecoveryType() {
        return PasswordRecoveryType.PHONE_NUMBER;
    }
}
