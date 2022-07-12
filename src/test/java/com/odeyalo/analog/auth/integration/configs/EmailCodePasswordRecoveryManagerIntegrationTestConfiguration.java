package com.odeyalo.analog.auth.integration.configs;

import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.repository.VerificationCodeRepository;
import com.odeyalo.analog.auth.service.facade.mail.KafkaMessageBrokerVerificationCodeMailSenderFacade;
import com.odeyalo.analog.auth.service.facade.mail.VerificationCodeMailSenderFacade;
import com.odeyalo.analog.auth.service.recovery.EmailCodePasswordRecoveryManager;
import com.odeyalo.analog.auth.service.recovery.PasswordRecoveryManagerFactory;
import com.odeyalo.analog.auth.service.sender.mail.KafkaBrokerMicroserviceDelegateMailSender;
import com.odeyalo.analog.auth.service.support.BcryptEncoderPasswordRecoverySaverSupport;
import com.odeyalo.analog.auth.service.support.PasswordRecoverySaverSupport;
import com.odeyalo.analog.auth.service.support.generatators.CodeGenerator;
import com.odeyalo.analog.auth.service.support.generatators.DigitCodeGenerator;
import com.odeyalo.analog.auth.service.support.verification.CodeVerificationManager;
import com.odeyalo.analog.auth.service.support.verification.EmailCodeVerificationManager;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Import(TestDatabaseConfiguration.class)
@TestConfiguration
public class EmailCodePasswordRecoveryManagerIntegrationTestConfiguration {

    @Bean
    public EmailCodePasswordRecoveryManager emailCodePasswordRecoveryManager(PasswordRecoverySaverSupport passwordRecoverySaverSupport,
                                                                             UserRepository userRepository,
                                                                             VerificationCodeMailSenderFacade verificationCodeMailSenderFacade,
                                                                             CodeVerificationManager codeVerificationManager) {
        return new EmailCodePasswordRecoveryManager(passwordRecoverySaverSupport,
                userRepository,
                verificationCodeMailSenderFacade,
                codeVerificationManager);
    }

    @Bean
    public PasswordRecoverySaverSupport passwordRecoverySaverSupport(UserRepository userRepository) {
        return new BcryptEncoderPasswordRecoverySaverSupport(passwordEncoder(), userRepository);
    }

    @Bean
    public VerificationCodeMailSenderFacade verificationCodeMailSenderFacade(@Qualifier("codeVerificationManager") CodeVerificationManager manager) {
        return new KafkaMessageBrokerVerificationCodeMailSenderFacade(Mockito.mock(KafkaBrokerMicroserviceDelegateMailSender.class), manager);
    }

    @Bean
    public CodeVerificationManager codeVerificationManager(VerificationCodeRepository verificationCodeRepository, CodeGenerator codeGenerator) {
        return new EmailCodeVerificationManager(verificationCodeRepository, codeGenerator);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CodeGenerator codeGenerator() {
        return new DigitCodeGenerator();
    }

    @Bean
    public PasswordRecoveryManagerFactory passwordRecoveryManagerFactory() {
        return new PasswordRecoveryManagerFactory();
    }
}
