package com.odeyalo.analog.auth.service.validators;

import com.odeyalo.analog.auth.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DefaultRequestUserDTOValidator implements RequestUserDTOValidator {
    private final Validator nicknameValidator;
    private final Validator emailValidator;
    private final Validator passwordValidator;
    private final Logger logger = LoggerFactory.getLogger(RequestUserDTOValidator.class);

    public DefaultRequestUserDTOValidator(@Qualifier("userNicknameValidator") Validator nicknameValidator,
                                   @Qualifier("userEmailValidator") Validator emailValidator,
                                   @Qualifier("userPasswordValidator") Validator passwordValidator) {
        this.nicknameValidator = nicknameValidator;
        this.emailValidator = emailValidator;
        this.passwordValidator = passwordValidator;
    }

    public ValidationResult validate(String email, String nickname, String password) throws ValidationException {
        if (!this.nicknameValidator.validate(nickname)) {
            this.logger.warn("Nickname must be more than 6 and less than 12 chars, nickname: {}", nickname);
            return new ValidationResult("Nickname must be more than 6 and less than 12 chars", false);
        }
        if (!this.emailValidator.validate(email)) {
            this.logger.error("Wrong email: {}", email);
            return new ValidationResult("Wrong email", false);

        }
        if(!this.passwordValidator.validate(password)) {
            this.logger.error("Password must be at least 8 chars and less than 50: {}" , password);
            return new ValidationResult("Password must be at least 8 chars and less than 50", false);
        }
        return new ValidationResult(true);
    }
}
