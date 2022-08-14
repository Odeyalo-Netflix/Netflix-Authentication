package com.odeyalo.analog.auth.config;

import com.odeyalo.analog.auth.exceptions.KeyConstructionException;
import com.odeyalo.analog.auth.support.*;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.PublicKey;

@Configuration
public class RsaTokenConfiguration {

    @Bean
    @Profile("read")
    public Pair<PublicKey, PrivateKey> readKeysFromFile(FileReader<PrivateKey> privateKeyFileReader,
                                                        FileReader<PublicKey> publicKeyFileReader,
                                                        @Value("${app.security.keys.rsa.private.filename}") String privateFileName,
                                                        @Value("${app.security.keys.rsa.public.filename}") String publicFileName) throws IOException, KeyConstructionException {
        PrivateKey privateKey = privateKeyFileReader.readFile(Paths.get(privateFileName));
        PublicKey publicKey = publicKeyFileReader.readFile(Paths.get(publicFileName));
        return Pair.of(publicKey, privateKey);
    }

    @Bean
    @Profile("write")
    public RsaTokenPairFileWriter rsaTokenPairFileWriter() {
        return new RsaTokenPairFileWriter();
    }
    @Bean
    @Profile("read")
    public PublicKeyFileReader publicKeyFileReader() {
        return new PublicKeyFileReader(keyGenerator());
    }
    @Bean
    @Profile("read")
    public PrivateKeyFileReader privateKeyFileReader() {
        return new PrivateKeyFileReader(keyGenerator());
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new DefaultKeyGenerator();
    }
}
