package com.odeyalo.analog.auth.config.security.jwt.utils;

import com.odeyalo.analog.auth.service.support.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SecretKeyJwtTokenProvider extends AbstractJwtTokenProvider {
    @Value("${security.jwt.time.expiration}")
    private Integer JWT_TOKEN_EXPIRATION_TIME;
    private JwtParser jwtParser;

    @Autowired
    public SecretKeyJwtTokenProvider(@Value("${security.jwt.secret}") String signingKey) {
        super(signingKey.getBytes());
        this.jwtParser = Jwts.parser().setSigningKey(signingKey.getBytes());
    }

    @Override
    public String generateJwtToken(final UserDetails details) {
        Map<String, Object> claims = new HashMap<>();
        CustomUserDetails customDetails = (CustomUserDetails) details;
        claims.put("id", customDetails.getUser().getId());
        claims.put("username", customDetails.getUsername());
        claims.put("roles", customDetails.getUser().getRoles());
        claims.put("nickname", customDetails.getUser().getNickname());
        return this.doGenerateJwtToken(claims, details.getUsername(), JWT_TOKEN_EXPIRATION_TIME, SignatureAlgorithm.HS256, signingKey);
    }

    @Override
    public boolean isTokenValid(final String token, final UserDetails details) {
        return this.getNicknameFromJwtToken(token).equals(details.getUsername()) && !this.isTokenExpired(token);
    }

    @Override
    public JwtParser getParser() {
        return jwtParser;
    }

    public String getNicknameFromJwtToken(String token) {
        Claims claims = this.getClaims(token);
        return (String) claims.get("nickname");
    }
}
