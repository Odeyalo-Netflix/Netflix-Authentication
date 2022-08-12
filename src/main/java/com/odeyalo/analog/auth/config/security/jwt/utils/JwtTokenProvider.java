package com.odeyalo.analog.auth.config.security.jwt.utils;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

/**
 * Provides methods to working with jwt token
 */
public interface JwtTokenProvider {
    /**
     *
     * @param userDetails - user data
     * @return - returns a generated jwt token
     */
    String generateJwtToken(UserDetails userDetails);

    /**
     *
     * @param token - token to check
     * @param userDetails - user data
     * @return - true if token is valid, otherwise - false
     */
    boolean isTokenValid(String token, UserDetails userDetails);

    /**
     *
     * @param token - token to check
     * @return - true if token is valid, otherwise - false
     */
    boolean validateToken(String token);

    /**
     * Returns the time the token expired
     * @param token - token to check
     * @return - date with token expire time
     */
    Date getExpiredJwtTokenTime(String token);

    /**
     * Returns body of this token
     * @param token - token to check
     * @return - token's body
     */
    Object getBody(String token);

    /**
     * Returns claims of this token
     * @param token - token to check
     * @return - token's claims
     */
    Claims getClaims(String token);

    /**
     * Checks if token is expired
     * @param token - token to check
     * @return - true if token expired, otherwise - false
     */
    boolean isTokenExpired(String token);
}
