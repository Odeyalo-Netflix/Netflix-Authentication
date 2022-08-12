package com.odeyalo.analog.auth.config.security.jwt.utils;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;

class RsaTokenPairGeneratorImplTest {

    @Test
    void getRsaTokens() throws NoSuchAlgorithmException {
        RsaTokenPairGeneratorImpl generator = new RsaTokenPairGeneratorImpl();
        Pair<PublicKey, PrivateKey> tokens = generator.getRsaTokens();
        PublicKey publicKey = tokens.getLeft();
        PrivateKey privateKey = tokens.getRight();
        System.out.println("privateKey" + privateKey.getEncoded());
        System.out.println("public key" + publicKey);
    }
}
