package com.odeyalo.analog.auth.service.facade;

import com.odeyalo.analog.auth.config.security.jwt.utils.JwtTokenProvider;
import com.odeyalo.analog.auth.dto.response.JwtTokenResponseDTO;
import com.odeyalo.analog.auth.entity.RefreshToken;
import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.service.login.LoginHandler;
import com.odeyalo.analog.auth.service.refresh.RefreshTokenHandler;
import com.odeyalo.analog.auth.service.support.CustomUserDetails;
import org.springframework.stereotype.Service;

@Service
public class UsernamePasswordLoginHandlerFacadeImpl implements UsernamePasswordLoginHandlerFacade {
    private final LoginHandler usernamePasswordLoginHandler;
    private final JwtTokenProvider provider;
    private final RefreshTokenHandler refreshTokenHandler;
    public UsernamePasswordLoginHandlerFacadeImpl(LoginHandler usernamePasswordLoginHandler,
                                                  JwtTokenProvider provider,
                                                  RefreshTokenHandler refreshTokenHandler) {
        this.usernamePasswordLoginHandler = usernamePasswordLoginHandler;
        this.provider = provider;
        this.refreshTokenHandler = refreshTokenHandler;
    }

    @Override
    public JwtTokenResponseDTO login(User dto) {
        User user = this.usernamePasswordLoginHandler.login(dto);
        String jwtToken = this.provider.generateJwtToken(new CustomUserDetails(user));
        RefreshToken refreshToken = this.refreshTokenHandler.createAndSaveToken(user);
        return new JwtTokenResponseDTO(true, jwtToken, refreshToken.getRefreshToken());
    }
}
