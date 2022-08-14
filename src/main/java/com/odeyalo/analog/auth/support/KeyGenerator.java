package com.odeyalo.analog.auth.support;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

/**
 * Generates public and private keys
 */
public interface KeyGenerator {

    PublicKey generatePublicKey(String algorithm, byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException;

    PrivateKey generatePrivateKey(String algorithm, byte[] signingKey) throws NoSuchAlgorithmException, InvalidKeySpecException;
}
