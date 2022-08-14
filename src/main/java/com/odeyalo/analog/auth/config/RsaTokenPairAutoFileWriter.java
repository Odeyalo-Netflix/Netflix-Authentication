package com.odeyalo.analog.auth.config;


import com.odeyalo.analog.auth.support.RsaTokenPairFileWriter;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Auto writes public and private key pair to specific file
 */
@Component
@Profile("write")
public class RsaTokenPairAutoFileWriter {
    private final Logger logger = LoggerFactory.getLogger(RsaTokenPairAutoFileWriter.class);

    @Autowired
    public void writeTokensToFile(RsaTokenPairFileWriter writer,
                                  Pair<PublicKey, PrivateKey> keys,
                                  @Value("${app.security.keys.rsa.private.filename}") String privateName,
                                  @Value("${app.security.keys.rsa.public.filename}") String publicName) throws IOException {
        this.logger.info("Public key file name: {}", publicName);
        this.logger.info("Private key file name: {}", privateName);
        byte[] publicKeyBytes = keys.getLeft().getEncoded();
        writer.write(Paths.get(publicName), publicKeyBytes);
        byte[] privateKeyBytes = keys.getRight().getEncoded();
        writer.write(Paths.get(privateName), privateKeyBytes);
    }
}
