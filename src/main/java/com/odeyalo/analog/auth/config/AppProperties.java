package com.odeyalo.analog.auth.config;


import com.odeyalo.analog.auth.config.security.jwt.utils.RsaTokenPairGenerator;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
    @Value("${app.oauth2.authorizedRedirectUris}")
    private final List<String> authorizedRedirectUris = new ArrayList<>(5);


    public List<String> getAuthorizedRedirectUris() {
        return authorizedRedirectUris;
    }

    @Bean
    @Profile("write")
    public Pair<PublicKey, PrivateKey> publicKeyPrivateKeyPair(RsaTokenPairGenerator generator) throws NoSuchAlgorithmException {
        return generator.getRsaTokens();
    }
}
