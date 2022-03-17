package com.odeyalo.analog.auth.service.support.verification;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.VerificationCode;
import com.odeyalo.analog.auth.repository.CodeRepository;
import com.odeyalo.analog.auth.service.support.generatators.CodeGenerator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class EmailCodeVerificationManager implements CodeVerificationManager {
    private final CodeRepository codeRepository;
    private final CodeGenerator codeGenerator;

    public EmailCodeVerificationManager(CodeRepository codeRepository, CodeGenerator codeGenerator) {
        this.codeRepository = codeRepository;
        this.codeGenerator = codeGenerator;
    }

    @Override
    public VerificationCode generateAndSave(User user, Integer codeLength, Integer activeMinutes) {
        String codeValue = this.codeGenerator.code(codeLength);
        VerificationCode verificationCode = VerificationCode.builder()
                .codeValue(codeValue)
                .user(user)
                .isActivated(false)
                .expired(LocalDateTime.now().plusMinutes(5))
                .build();
        return this.codeRepository.save(verificationCode);
    }

    @Override
    public Optional<VerificationCode> getVerificationCodeByCodeValue(String codeValue) {
        return this.codeRepository.findCodeByCodeValue(codeValue);
    }

    @Override
    public boolean verifyCode(String code) {
        Optional<VerificationCode> codeOptional = this.codeRepository.findCodeByCodeValue(code);
        return codeOptional.isPresent();
    }

    @Override
    public void deleteCode(VerificationCode verificationCode) {
        this.codeRepository.delete(verificationCode);
    }

    @Override
    public void deleteCode(String codeValue) {
        this.codeRepository.deleteByCodeValue(codeValue);
    }
}
