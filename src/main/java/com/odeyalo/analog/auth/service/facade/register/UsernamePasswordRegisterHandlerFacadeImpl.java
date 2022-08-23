package com.odeyalo.analog.auth.service.facade.register;

import com.odeyalo.analog.auth.config.security.jwt.utils.JwtTokenProvider;
import com.odeyalo.analog.auth.dto.response.JwtTokenResponseDTO;
import com.odeyalo.analog.auth.entity.RefreshToken;
import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.service.events.EventHandlerManager;
import com.odeyalo.analog.auth.service.events.register.AbstractUserRegisteredEventHandler;
import com.odeyalo.analog.auth.service.events.register.UserRegisteredEvent;
import com.odeyalo.analog.auth.service.refresh.RefreshTokenProvider;
import com.odeyalo.analog.auth.service.register.RegisterHandler;
import com.odeyalo.analog.auth.service.support.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;

@Service
public class UsernamePasswordRegisterHandlerFacadeImpl implements UsernamePasswordRegisterHandlerFacade {
    private final JwtTokenProvider tokenProvider;
    private final RegisterHandler registerHandler;
    private final RefreshTokenProvider refreshTokenProvider;
    private final EventHandlerManager eventManager;

    @Autowired
    public UsernamePasswordRegisterHandlerFacadeImpl(JwtTokenProvider tokenProvider, @Qualifier("usernamePasswordRegisterHandler") RegisterHandler registerHandler,
                                                     RefreshTokenProvider refreshTokenProvider,
                                                     @Qualifier("asyncEventHandlerManager") EventHandlerManager eventManager) {
        this.tokenProvider = tokenProvider;
        this.registerHandler = registerHandler;
        this.refreshTokenProvider = refreshTokenProvider;
        this.eventManager = eventManager;
    }

    @Override
    public JwtTokenResponseDTO save(User user) throws AuthException {
        User registeredUser = this.registerHandler.register(user);
        String jwtToken = this.tokenProvider.generateJwtToken(new CustomUserDetails(user));
        RefreshToken refreshToken = this.refreshTokenProvider.createAndSaveToken(user);
        this.eventManager.notifySpecialEventHandlers(AbstractUserRegisteredEventHandler.USER_REGISTERED_EVENT_VALUE, new UserRegisteredEvent(registeredUser));
        return new JwtTokenResponseDTO(true, jwtToken, refreshToken.getRefreshToken());
    }
}
