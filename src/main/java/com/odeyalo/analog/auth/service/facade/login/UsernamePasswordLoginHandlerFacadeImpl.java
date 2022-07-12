package com.odeyalo.analog.auth.service.facade.login;

import com.odeyalo.analog.auth.config.security.jwt.utils.JwtTokenProvider;
import com.odeyalo.analog.auth.dto.response.JwtTokenResponseDTO;
import com.odeyalo.analog.auth.entity.RefreshToken;
import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.exceptions.LoginException;
import com.odeyalo.analog.auth.service.login.LoginHandler;
import com.odeyalo.analog.auth.service.refresh.RefreshTokenProvider;
import com.odeyalo.analog.auth.service.support.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UsernamePasswordLoginHandlerFacadeImpl implements UsernamePasswordLoginHandlerFacade {
    private final LoginHandler usernamePasswordLoginHandler;
    private final JwtTokenProvider provider;
    private final RefreshTokenProvider refreshTokenProvider;

    @Autowired
    public UsernamePasswordLoginHandlerFacadeImpl(@Qualifier("usernamePasswordLoginHandler") LoginHandler usernamePasswordLoginHandler,
                                                  JwtTokenProvider provider,
                                                  RefreshTokenProvider refreshTokenProvider) {
        this.usernamePasswordLoginHandler = usernamePasswordLoginHandler;
        this.provider = provider;
        this.refreshTokenProvider = refreshTokenProvider;
    }

    @Override
    public JwtTokenResponseDTO login(User dto) throws LoginException {
        User user = this.usernamePasswordLoginHandler.login(dto);
        String jwtToken = this.provider.generateJwtToken(new CustomUserDetails(user));
        RefreshToken refreshToken = this.refreshTokenProvider.createAndSaveToken(user);
        return new JwtTokenResponseDTO(true, jwtToken, refreshToken.getRefreshToken());
    }
}
