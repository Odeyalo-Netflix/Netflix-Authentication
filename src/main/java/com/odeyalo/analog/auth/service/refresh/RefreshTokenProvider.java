package com.odeyalo.analog.auth.service.refresh;

import com.odeyalo.analog.auth.entity.RefreshToken;
import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.exceptions.RefreshTokenException;

public interface RefreshTokenProvider {

    RefreshToken createAndSaveToken(User user);

    boolean verifyToken(RefreshToken refreshToken) throws RefreshTokenException;

    void deleteTokenByUserId(Integer userId);

}
