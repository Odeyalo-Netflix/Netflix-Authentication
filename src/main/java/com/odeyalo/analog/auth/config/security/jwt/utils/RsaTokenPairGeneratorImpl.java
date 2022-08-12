package com.odeyalo.analog.auth.config.security.jwt.utils;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.security.*;

@Component
public class RsaTokenPairGeneratorImpl implements RsaTokenPairGenerator {

    @Override
    public Pair<PublicKey, PrivateKey> getRsaTokens() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();
        return Pair.of(publicKey, privateKey);
    }
}
