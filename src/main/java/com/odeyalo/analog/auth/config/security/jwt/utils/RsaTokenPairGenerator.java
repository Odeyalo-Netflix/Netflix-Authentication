package com.odeyalo.analog.auth.config.security.jwt.utils;

import org.apache.commons.lang3.tuple.Pair;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface RsaTokenPairGenerator {
    /**
     * Generate a pair of public and private keys for jwt token
     *
     * @return - pair with public and private key
     */
    Pair<PublicKey, PrivateKey> getRsaTokens() throws NoSuchAlgorithmException;
}
