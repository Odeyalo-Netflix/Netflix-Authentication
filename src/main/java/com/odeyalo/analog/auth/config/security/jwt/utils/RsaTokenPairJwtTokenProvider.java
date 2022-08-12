package com.odeyalo.analog.auth.config.security.jwt.utils;

import com.odeyalo.analog.auth.exceptions.JwtParserConstructionException;
import com.odeyalo.analog.auth.service.support.CustomUserDetails;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Primary
public class RsaTokenPairJwtTokenProvider extends AbstractJwtTokenProvider {
    private final PrivateKey privateSigningKey;
    private JwtParser parser;
    @Value("${security.jwt.time.expiration}")
    private Integer JWT_TOKEN_EXPIRATION_TIME;

    @Autowired
    public RsaTokenPairJwtTokenProvider(Pair<PublicKey, PrivateKey> keys) {
        super(keys.getLeft().getEncoded());
        this.privateSigningKey = keys.getRight();
    }

    @PostConstruct
    public void init() throws JwtParserConstructionException {
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey key = factory.generatePublic(new X509EncodedKeySpec(signingKey));
            this.parser = Jwts.parser().setSigningKey(key);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            this.logger.error("Cannot construct jwt parser with given error: ", e);
            throw new JwtParserConstructionException("Cannot construct jwt parser");
        }
    }
    @Override
    public String generateJwtToken(UserDetails userDetails) {
        CustomUserDetails customDetails = (CustomUserDetails) userDetails;
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", customDetails.getUser().getId());
        claims.put("roles", customDetails.getUser().getRoles());
        claims.put("username", customDetails.getUsername());
        claims.put("nickname", customDetails.getUser().getNickname());
        return this.doGenerateJwtToken(claims, userDetails.getUsername(), JWT_TOKEN_EXPIRATION_TIME, SignatureAlgorithm.RS512, privateSigningKey);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return this.getNicknameFromToken(token).equals(userDetails.getUsername()) && !this.isTokenExpired(token);
    }

    @Override
    public JwtParser getParser() {
        return parser;
    }

    protected String doGenerateJwtToken(Map<String, Object> claims, String subject, Integer expiredTime, SignatureAlgorithm algorithm, PrivateKey key) {
        return Jwts.builder().setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expiredTime * 1000L))
                .setSubject(subject)
                .signWith(algorithm, key)
                .compact();
    }
}
