package com.odeyalo.analog.auth.integration.configs;

import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.service.support.BcryptEncoderPasswordRecoverySaverSupport;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
@Import(TestDatabaseConfiguration.class)
public class BcryptEncoderPasswordRecoverySaverSupportTestConfiguration {

    @Bean
    public BcryptEncoderPasswordRecoverySaverSupport bcryptEncoderPasswordRecoverySaverSupport(UserRepository userRepository, PasswordEncoder encoder) {
        return new BcryptEncoderPasswordRecoverySaverSupport(encoder, userRepository);
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


}
