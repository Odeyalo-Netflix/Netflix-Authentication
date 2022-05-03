package com.odeyalo.analog.auth.integration.configs;

import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.repository.VerificationCodeRepository;
import com.odeyalo.analog.auth.service.recovery.PasswordRecoveryManagerFactory;
import com.odeyalo.analog.auth.service.recovery.PhoneNumberSMSPasswordRecoveryManager;
import com.odeyalo.analog.auth.service.sender.sms.KafkaMessageBrokerMicroserviceDelegatePhoneNumberMessageSender;
import com.odeyalo.analog.auth.service.sender.sms.PhoneNumberMessageSender;
import com.odeyalo.analog.auth.service.support.BcryptEncoderPasswordRecoverySaverSupport;
import com.odeyalo.analog.auth.service.support.PasswordRecoverySaverSupport;
import com.odeyalo.analog.auth.service.support.generatators.CodeGenerator;
import com.odeyalo.analog.auth.service.support.generatators.DigitCodeGenerator;
import com.odeyalo.analog.auth.service.support.verification.CodeVerificationManager;
import com.odeyalo.analog.auth.service.support.verification.PhoneNumberCodeVerificationManager;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
@Import(TestDatabaseConfiguration.class)
public class PhoneNumberSMSPasswordRecoveryManagerIntegrationTestConfiguration {

    @Bean
    public PhoneNumberSMSPasswordRecoveryManager passwordRecoveryManager(PhoneNumberMessageSender phoneNumberMessageSender,
                                                                         CodeVerificationManager codeVerificationManager,
                                                                         PasswordRecoverySaverSupport passwordRecoverySaverSupport,
                                                                         UserRepository repository) {
        return new PhoneNumberSMSPasswordRecoveryManager(phoneNumberMessageSender, codeVerificationManager, passwordRecoverySaverSupport, repository);
    }

    @Bean
    public PhoneNumberMessageSender phoneNumberMessageSender() {
        return Mockito.mock(KafkaMessageBrokerMicroserviceDelegatePhoneNumberMessageSender.class);
    }

    @Bean
    public CodeVerificationManager codeVerificationManager(VerificationCodeRepository verificationCodeRepository, CodeGenerator codeGenerator) {
        return new PhoneNumberCodeVerificationManager(verificationCodeRepository, codeGenerator);
    }
    @Bean
    public PasswordRecoverySaverSupport passwordRecoverySaverSupport(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        return new BcryptEncoderPasswordRecoverySaverSupport(passwordEncoder, userRepository);
    }

    @Bean
    public PasswordRecoveryManagerFactory passwordRecoveryManagerFactory() {
        return new PasswordRecoveryManagerFactory();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CodeGenerator codeGenerator() {
        return new DigitCodeGenerator();
    }

}
