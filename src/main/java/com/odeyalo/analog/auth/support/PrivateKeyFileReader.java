package com.odeyalo.analog.auth.support;

import com.odeyalo.analog.auth.exceptions.KeyConstructionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

public class PrivateKeyFileReader implements FileReader<PrivateKey> {
    private final KeyGenerator keyGenerator;
    private final Logger logger = LoggerFactory.getLogger(PrivateKeyFileReader.class);

    @Autowired
    public PrivateKeyFileReader(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    @Override
    public PrivateKey readFile(Path path) throws IOException, KeyConstructionException {
        try {
            byte[] bytes = Files.readAllBytes(path);
            return this.keyGenerator.generatePrivateKey("RSA", bytes);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            this.logger.error("Public key construction was failed.", e);
            throw new KeyConstructionException("Cannot construct a public key. Nested exception is: ", e);
        }
    }
}
