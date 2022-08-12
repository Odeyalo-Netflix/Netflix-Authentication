package com.odeyalo.analog.auth.config.security.jwt.utils;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;

public abstract class AbstractJwtTokenProvider implements JwtTokenProvider {
    protected final byte[] signingKey;
    protected final Logger logger = LoggerFactory.getLogger(AbstractJwtTokenProvider.class);

    protected AbstractJwtTokenProvider(byte[] signingKey) {
        this.signingKey = signingKey;
    }

    @Override
    public abstract String generateJwtToken(UserDetails userDetails);

    @Override
    public abstract boolean isTokenValid(String token, UserDetails userDetails);

    /**
     * Returns parser with SIGNING KEY to get token data
     * @return Returns parser with signing key
     */
    public abstract JwtParser getParser();

    @Override
    public boolean validateToken(String token) {
        try {
            getParser().parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            this.logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            this.logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            this.logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            this.logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            this.logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public Date getExpiredJwtTokenTime(String token) {
        Claims claims = this.getClaims(token);
        return claims.getExpiration();
    }

    @Override
    public Object getBody(String token) {
        return getParser().parse(token).getBody();
    }

    @Override
    public Claims getClaims(String token) {
        try {
            return getParser().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    @Override
    public boolean isTokenExpired(String token) {
        final Date tokenTime = this.getExpiredJwtTokenTime(token);
        return tokenTime.before(new Date());
    }

    @Override
    public String getNicknameFromToken(String token) {
        Claims claims = this.getClaims(token);
        return (String) claims.get("nickname");
    }

    /**
     *
     * @param claims - claims for jwt token
     * @param subject - subject
     * @param expiredTime - expired time for token in MILLISECONDS
     * @return - returns a jwt token with claims and subject
     */
    protected String doGenerateJwtToken(Map<String, Object> claims,
                                        String subject,
                                        Integer expiredTime,
                                        SignatureAlgorithm algorithm,
                                        byte[] key) {
        return Jwts.builder().setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expiredTime * 1000L))
                .setSubject(subject)
                .signWith(algorithm, key)
                .compact();
    }
}
