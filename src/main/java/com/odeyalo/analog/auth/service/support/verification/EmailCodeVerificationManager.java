package com.odeyalo.analog.auth.service.support.verification;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.VerificationCode;
import com.odeyalo.analog.auth.repository.VerificationCodeRepository;
import com.odeyalo.analog.auth.service.support.generatators.CodeGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class EmailCodeVerificationManager implements CodeVerificationManager {
    private final VerificationCodeRepository verificationCodeRepository;
    private final CodeGenerator codeGenerator;

    public EmailCodeVerificationManager(VerificationCodeRepository verificationCodeRepository,
                                        @Qualifier("digitCodeGenerator") CodeGenerator codeGenerator) {
        this.verificationCodeRepository = verificationCodeRepository;
        this.codeGenerator = codeGenerator;
    }

    @Override
    @Transactional
    public VerificationCode generateAndSave(User user, Integer codeLength, Integer activeMinutes) {
        String codeValue = this.codeGenerator.code(codeLength);
        VerificationCode verificationCode = VerificationCode.builder()
                .codeValue(codeValue)
                .user(user)
                .isActivated(false)
                .expired(LocalDateTime.now().plusMinutes(5))
                .build();
        return this.verificationCodeRepository.save(verificationCode);
    }

    @Override
    @Transactional
    public Optional<VerificationCode> getVerificationCodeByCodeValue(String codeValue) {
        return this.verificationCodeRepository.findCodeByCodeValue(codeValue);
    }

    @Override
    @Transactional
    public boolean verifyCode(String code) {
        Optional<VerificationCode> codeOptional = this.verificationCodeRepository.findCodeByCodeValue(code);
        return codeOptional.isPresent();
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
