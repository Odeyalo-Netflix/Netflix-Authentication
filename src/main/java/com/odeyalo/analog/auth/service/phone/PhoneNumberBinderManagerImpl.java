package com.odeyalo.analog.auth.service.phone;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.VerificationCode;
import com.odeyalo.analog.auth.exceptions.CodeVerificationException;
import com.odeyalo.analog.auth.exceptions.PhoneNumberBindException;
import com.odeyalo.analog.auth.service.sender.sms.PhoneNumberMessageSender;
import com.odeyalo.analog.auth.service.support.generatators.CodeGenerator;
import com.odeyalo.analog.auth.service.support.verification.CodeVerificationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PhoneNumberBinderManagerImpl implements PhoneNumberBinderManager {
    private final UserPhoneNumberBinder phoneNumberBinder;
    private final PhoneNumberMessageSender messageSender;
    private final CodeVerificationManager codeVerificationManager;
    private final Logger logger = LoggerFactory.getLogger(PhoneNumberBinderManagerImpl.class);
    private final static String DEFAULT_MESSAGE = "You want to change your phone number. Please confirm it using this code: %s. If you didn't do it, please, change your password";

    @Autowired
    public PhoneNumberBinderManagerImpl(UserPhoneNumberBinder phoneNumberBinder,
                                        @Qualifier("kafkaMessageBrokerMicroserviceDelegatePhoneNumberMessageSender") PhoneNumberMessageSender messageSender,
                                        @Qualifier("phoneNumberCodeVerificationManager") CodeVerificationManager codeVerificationManager) {
        this.phoneNumberBinder = phoneNumberBinder;
        this.messageSender = messageSender;
        this.codeVerificationManager = codeVerificationManager;
    }

    @Override
    public void sendVerificationCode(User user, String phoneNumber) {
        VerificationCode verificationCode = this.codeVerificationManager.generateAndSave(user, CodeVerificationManager.DEFAULT_ACTIVE_MINUTES, CodeVerificationManager.DEFAULT_ACTIVE_MINUTES);
        String codeValue = verificationCode.getCodeValue();
        this.messageSender.sendMessage(phoneNumber, String.format(DEFAULT_MESSAGE, codeValue));
    }

    @Override
    public void changeUserPhoneIfCodeCorrect(String code, String newPhoneNumber) {
        Optional<VerificationCode> optional = this.codeVerificationManager.getVerificationCodeByCodeValue(code);
        if (!optional.isPresent()) {
            this.logger.info("Code: {} isn't present in DB", code);
            throw new CodeVerificationException(String.format("Code: %s is not correct. Please try again", code));
        }
        VerificationCode verificationCode = optional.get();
        if (verificationCode.isActivated()) {
            this.logger.info("Code is activated: {}", verificationCode);
            throw new CodeVerificationException("The code is already activated. Please try another code");
        }
        if (verificationCode.getExpired().isAfter(LocalDateTime.now())) {
            this.logger.info("Code is expired: {}", verificationCode);
            throw new CodeVerificationException("The code is already expired. Please try another code");
        }
        User user = verificationCode.getUser();
        user.setPhoneNumber(newPhoneNumber);
        this.phoneNumberBinder.bindUserPhone(user, newPhoneNumber);
    }

    @Override
    public boolean confirmCode(String code) {
        Optional<VerificationCode> verificationCode = this.codeVerificationManager.getVerificationCodeByCodeValue(code);
        return verificationCode.isPresent() && !verificationCode.get().isActivated() && verificationCode.get().getExpired().isAfter(LocalDateTime.now());
    }
}
