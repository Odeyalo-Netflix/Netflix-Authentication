package com.odeyalo.analog.auth.service.facade;

import com.odeyalo.analog.auth.config.security.jwt.utils.JwtTokenProvider;
import com.odeyalo.analog.auth.dto.response.JwtTokenResponseDTO;
import com.odeyalo.analog.auth.entity.RefreshToken;
import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.service.refresh.RefreshTokenProvider;
import com.odeyalo.analog.auth.service.register.RegisterHandler;
import com.odeyalo.analog.auth.service.support.CustomUserDetails;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;

@Service
public class UsernamePasswordRegisterHandlerFacadeImpl implements UsernamePasswordRegisterHandlerFacade {
    private final JwtTokenProvider tokenProvider;
    private final RegisterHandler registerHandler;
    private final RefreshTokenProvider refreshTokenProvider;

    public UsernamePasswordRegisterHandlerFacadeImpl(JwtTokenProvider tokenProvider, @Qualifier("usernamePasswordRegisterHandler") RegisterHandler registerHandler, RefreshTokenProvider refreshTokenProvider) {
        this.tokenProvider = tokenProvider;
        this.registerHandler = registerHandler;
        this.refreshTokenProvider = refreshTokenProvider;
    }

    @Override
    public JwtTokenResponseDTO save(User user) throws AuthException {
        this.registerHandler.register(user);
        String jwtToken = this.tokenProvider.generateJwtToken(new CustomUserDetails(user));
        RefreshToken refreshToken = this.refreshTokenProvider.createAndSaveToken(user);
        return new JwtTokenResponseDTO(true, jwtToken, refreshToken.getRefreshToken());
    }
}
