package com.odeyalo.analog.auth.service.facade.login;

import com.odeyalo.analog.auth.config.security.jwt.utils.JwtTokenProvider;
import com.odeyalo.analog.auth.dto.response.JwtTokenResponseDTO;
import com.odeyalo.analog.auth.entity.RefreshToken;
import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.exceptions.LoginException;
import com.odeyalo.analog.auth.service.events.EventHandlerManager;
import com.odeyalo.analog.auth.service.events.EventType;
import com.odeyalo.analog.auth.service.events.UserLoggedInEvent;
import com.odeyalo.analog.auth.service.login.LoginHandler;
import com.odeyalo.analog.auth.service.refresh.RefreshTokenProvider;
import com.odeyalo.analog.auth.service.support.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UsernamePasswordLoginHandlerFacadeImpl implements UsernamePasswordLoginHandlerFacade {
    private final LoginHandler usernamePasswordLoginHandler;
    private final JwtTokenProvider provider;
    private final RefreshTokenProvider refreshTokenProvider;
    private final EventHandlerManager eventHandler;

    @Autowired
    public UsernamePasswordLoginHandlerFacadeImpl(@Qualifier("usernamePasswordLoginHandler") LoginHandler usernamePasswordLoginHandler,
                                                  JwtTokenProvider provider,
                                                  RefreshTokenProvider refreshTokenProvider, EventHandlerManager eventHandler) {
        this.usernamePasswordLoginHandler = usernamePasswordLoginHandler;
        this.provider = provider;
        this.refreshTokenProvider = refreshTokenProvider;
        this.eventHandler = eventHandler;
    }

    @Override
    public JwtTokenResponseDTO login(User dto) throws LoginException {
        User user = this.usernamePasswordLoginHandler.login(dto);
        String jwtToken = this.provider.generateJwtToken(new CustomUserDetails(user));
        RefreshToken refreshToken = this.refreshTokenProvider.createAndSaveToken(user);
        fireUserLoggedInEvent(user);
        return new JwtTokenResponseDTO(true, jwtToken, refreshToken.getRefreshToken());
    }


    private void fireUserLoggedInEvent(User user) {
        this.eventHandler.notifySpecialEventHandlers(EventType.USER_LOGGED_IN, new UserLoggedInEvent(user, LocalDateTime.now()));
    }
}
