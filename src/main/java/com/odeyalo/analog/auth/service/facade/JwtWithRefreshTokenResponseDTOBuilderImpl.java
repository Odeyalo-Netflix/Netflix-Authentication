package com.odeyalo.analog.auth.service.facade;

import com.odeyalo.analog.auth.config.security.jwt.utils.JwtTokenProvider;
import com.odeyalo.analog.auth.dto.response.RefreshTokenResponseDTO;
import com.odeyalo.analog.auth.entity.RefreshToken;
import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.exceptions.RefreshTokenException;
import com.odeyalo.analog.auth.repository.RefreshTokenRepository;
import com.odeyalo.analog.auth.service.support.CustomUserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtWithRefreshTokenResponseDTOBuilderImpl implements JwtWithRefreshTokenResponseDTOBuilder {
    private final RefreshTokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtWithRefreshTokenResponseDTOBuilderImpl(RefreshTokenRepository tokenRepository, JwtTokenProvider jwtTokenProvider) {
        this.tokenRepository = tokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public RefreshTokenResponseDTO generateResponseDTO(String token) throws RefreshTokenException {
        Optional<RefreshToken> tokenOptional = this.tokenRepository.findRefreshTokenByRefreshToken(token);
        if (!tokenOptional.isPresent()) {
            throw new RefreshTokenException("Refresh token is not in database!");
        }
        RefreshToken refreshToken = tokenOptional.get();
        User user = refreshToken.getUser();
        String jwtToken = this.jwtTokenProvider.generateJwtToken(new CustomUserDetails(user));
        return new RefreshTokenResponseDTO(jwtToken, refreshToken.getRefreshToken());
    }
}
