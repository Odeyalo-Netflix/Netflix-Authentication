package com.odeyalo.analog.auth.service.facade;

import com.odeyalo.analog.auth.dto.response.JwtTokenResponseDTO;
import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.service.events.EventHandlerManager;
import com.odeyalo.analog.auth.service.facade.mail.VerificationCodeMailSenderFacade;
import com.odeyalo.analog.auth.service.facade.register.UsernamePasswordRegisterHandlerFacade;
import com.odeyalo.analog.auth.service.register.RegisterHandler;
import com.odeyalo.analog.auth.service.support.generatators.CodeGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.security.auth.message.AuthException;

@Component
public class EmailVerificationUsernamePasswordRegisterHandlerFacade implements UsernamePasswordRegisterHandlerFacade {
    private final RegisterHandler registerHandler;
    private final VerificationCodeMailSenderFacade mailSender;
    private final EventHandlerManager eventHandlerManager;
    private final static String EMAIL_CONFIRMATION_LETTER_SENT_EVENT = "EMAIL_CONFIRMATION_LETTER_SENT_EVENT";

    public EmailVerificationUsernamePasswordRegisterHandlerFacade(@Qualifier("usernamePasswordRegisterHandler") RegisterHandler registerHandler,
                                                                  VerificationCodeMailSenderFacade mailSender, EventHandlerManager eventHandlerManager) {
        this.registerHandler = registerHandler;
        this.mailSender = mailSender;
        this.eventHandlerManager = eventHandlerManager;
    }

    @Override
    public JwtTokenResponseDTO save(User user) throws AuthException {
        this.registerHandler.register(user);
        this.mailSender.generateAndSend(user, CodeGenerator.DEFAULT_CODE_LENGTH);
//        this.eventHandlerManager.notifySpecialEventHandlers("");
        return new JwtTokenResponseDTO(false, null, null);
    }
}
