package com.odeyalo.analog.auth.service.support.verification;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.VerificationCode;
import com.odeyalo.analog.auth.repository.VerificationCodeRepository;
import com.odeyalo.analog.auth.service.support.generatators.CodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class PhoneNumberCodeVerificationManager implements CodeVerificationManager {
    private final VerificationCodeRepository verificationCodeRepository;
    private final CodeGenerator codeGenerator;
    private final Logger logger = LoggerFactory.getLogger(PhoneNumberCodeVerificationManager.class);

    public PhoneNumberCodeVerificationManager(VerificationCodeRepository verificationCodeRepository, @Qualifier("digitCodeGenerator") CodeGenerator codeGenerator) {
        this.verificationCodeRepository = verificationCodeRepository;
        this.codeGenerator = codeGenerator;
    }

    @Override
    @Transactional
    public VerificationCode generateAndSave(User user, Integer codeLength, Integer activeMinutes) {
        String code = this.codeGenerator.code(codeLength);
        VerificationCode verificationCode = VerificationCode.builder()
                .user(user)
                .expired(LocalDateTime.now().plusMinutes(activeMinutes))
                .isActivated(false)
                .codeValue(code)
                .build();
        VerificationCode save = this.verificationCodeRepository.save(verificationCode);
        this.logger.info("Save verification code: {}", save.getId());
        return save;
    }

    @Override
    @Transactional
    public Optional<VerificationCode> getVerificationCodeByCodeValue(String codeValue) {
        return this.verificationCodeRepository.findCodeByCodeValue(codeValue);
    }

    @Override
    @Transactional
    public boolean verifyCode(String code) {
        Optional<VerificationCode> codeByCodeValue = verificationCodeRepository.findCodeByCodeValue(code);
        return codeByCodeValue.isPresent() && codeByCodeValue.get().getExpired().isAfter(LocalDateTime.now());
    }

    @Override
    @Transactional
    public void deleteCode(VerificationCode verificationCode) {
        this.verificationCodeRepository.delete(verificationCode);
    }

    @Override
    @Transactional
    public void deleteCode(String codeValue) {
        this.verificationCodeRepository.deleteByCodeValue(codeValue);
    }
}
