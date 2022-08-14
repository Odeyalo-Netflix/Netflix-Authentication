package com.odeyalo.analog.auth.support;

import com.odeyalo.analog.auth.exceptions.KeyConstructionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;


public class PublicKeyFileReader implements FileReader<PublicKey> {
    private final KeyGenerator keyGenerator;
    private final Logger logger = LoggerFactory.getLogger(PublicKeyFileReader.class);

    @Autowired
    public PublicKeyFileReader(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    @Override
    public PublicKey readFile(Path path) throws IOException, KeyConstructionException {
        try {
            byte[] bytes = Files.readAllBytes(path);
            return this.keyGenerator.generatePublicKey("RSA", bytes);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            this.logger.error("Public key construction was failed.", e);
            throw new KeyConstructionException("Cannot construct a public key. Nested exception is: ", e);
        }
    }
}
