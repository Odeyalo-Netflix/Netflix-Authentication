package com.odeyalo.analog.auth.service.support.verification;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.VerificationCode;
import com.odeyalo.analog.auth.repository.VerificationCodeRepository;
import com.odeyalo.analog.auth.service.support.generatators.CodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

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
    public Optional<VerificationCode> getVerificationCodeByCodeValue(String codeValue) {
        return this.verificationCodeRepository.findCodeByCodeValue(codeValue);
    }

    @Override
    public boolean verifyCode(String code) {
        Optional<VerificationCode> codeByCodeValue = verificationCodeRepository.findCodeByCodeValue(code);
        return codeByCodeValue.isPresent() && codeByCodeValue.get().getExpired().isAfter(LocalDateTime.now());
    }

    @Override
    public void deleteCode(VerificationCode verificationCode) {
        this.verificationCodeRepository.delete(verificationCode);
    }

    @Override
    public void deleteCode(String codeValue) {
        Optional<VerificationCode> verificationCodeOptional = this.verificationCodeRepository.findCodeByCodeValue(codeValue);
        if (verificationCodeOptional.isPresent()) {
            VerificationCode verificationCode = verificationCodeOptional.get();
            this.deleteCode(verificationCode);
        }
    }
}
