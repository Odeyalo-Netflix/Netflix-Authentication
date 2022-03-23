package com.odeyalo.analog.auth.config.security.jwt.utils;

import com.odeyalo.analog.auth.service.support.CustomUserDetails;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
    private final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);
    @Value("${security.jwt.secret}")
    private String JWT_SECRET;
    @Value("${security.jwt.time.expiration}")
    private Integer JWT_TOKEN_EXPIRATION_TIME;

    public JwtTokenProvider() {}

    public String generateJwtToken(final UserDetails details) {
        Map<String, Object> claims = new HashMap<>();
        CustomUserDetails customDetails = (CustomUserDetails) details;
        claims.put("id", customDetails.getUser().getId());
        claims.put("username", customDetails.getUsername());
        claims.put("nickname", customDetails.getUser().getNickname());
        return this.doGenerateJwtToken(claims, details.getUsername());
    }

    public boolean isTokenValid(final String token, final UserDetails details) {
        return this.getNicknameFromJwtToken(token).equals(details.getUsername()) && !this.isTokenExpired(token);
    }

    public String getNicknameFromJwtToken(String token) {
        Claims claims = this.getClaims(token);
        return (String) claims.get("nickname");
    }

    public boolean validateToken(final String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            this.LOGGER.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            this.LOGGER.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            this.LOGGER.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            this.LOGGER.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            this.LOGGER.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public Date getExpiredJwtTokenTime(final String token) {
        Claims claims = this.getClaims(token);
        return claims.getExpiration();
    }

    public Object getBody(final String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET).parse(token).getBody();
    }

    public Claims getClaims(final String token) {
        try {
            return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public boolean isTokenExpired(final String token) {
        final Date tokenTime = this.getExpiredJwtTokenTime(token);
        return tokenTime.before(new Date());
    }

    private String doGenerateJwtToken(final Map<String, Object> claims, final String subject) {
        return Jwts.builder().setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_EXPIRATION_TIME * 1000L))
                .setSubject(subject)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }
}
