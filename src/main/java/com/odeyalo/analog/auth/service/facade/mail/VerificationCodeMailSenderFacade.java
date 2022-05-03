package com.odeyalo.analog.auth.service.facade.mail;

import com.odeyalo.analog.auth.entity.User;

/**
 * Generate verification code and sends it using MailSender
 */
public interface VerificationCodeMailSenderFacade {

    void generateAndSend(User user, Integer codeLength);

}
