package com.odeyalo.analog.auth.service.support.verification;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.VerificationCode;

import java.util.Optional;

public interface CodeVerificationManager {

    Integer DEFAULT_ACTIVE_MINUTES = 5;

    VerificationCode generateAndSave(User user, Integer codeLength, Integer activeMinutes);

    Optional<VerificationCode> getVerificationCodeByCodeValue(String codeValue);

    boolean verifyCode(String code);

    void deleteCode(VerificationCode codeValue);

    void deleteCode(String codeValue);
}
