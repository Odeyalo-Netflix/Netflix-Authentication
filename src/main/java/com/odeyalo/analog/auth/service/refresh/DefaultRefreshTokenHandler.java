package com.odeyalo.analog.auth.service.refresh;

import com.odeyalo.analog.auth.entity.RefreshToken;
import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.exceptions.RefreshTokenException;
import com.odeyalo.analog.auth.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;

@Service
public class DefaultRefreshTokenHandler implements RefreshTokenHandler {
    private final RefreshTokenGenerator tokenGenerator;
    private final RefreshTokenRepository tokenRepository;
    @Value("${security.token.refresh.time.expiration}")
    private Integer time;

    public DefaultRefreshTokenHandler(RefreshTokenGenerator tokenGenerator, RefreshTokenRepository tokenRepository) {
        this.tokenGenerator = tokenGenerator;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public RefreshToken createAndSaveToken(User user) {
        String token = this.tokenGenerator.generate();
        RefreshToken build = RefreshToken.builder()
                .refreshToken(token)
                .expireDate(Instant.now().plusSeconds(this.time))
                .user(user)
                .build();
        this.tokenRepository.save(build);
        return build;
    }

    @Override
    public boolean verifyToken(RefreshToken refreshToken) throws RefreshTokenException {
        if (refreshToken.getExpireDate().compareTo(Instant.now()) < 0) {
            this.tokenRepository.delete(refreshToken);
            throw new RefreshTokenException("Refresh token is expired");
        }
        return true;
    }


    @Override
    @Transactional
    public void deleteTokenByUserId(Integer userId) {
            this.tokenRepository.deleteRefreshTokenByUserId(userId);
    }
}
