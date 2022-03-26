package com.odeyalo.analog.auth.unit.service.facade.mail;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.VerificationCode;
import com.odeyalo.analog.auth.entity.enums.AuthProvider;
import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.repository.VerificationCodeRepository;
import com.odeyalo.analog.auth.service.facade.mail.VerificationCodeMailSenderFacadeImpl;
import com.odeyalo.analog.auth.service.register.mail.MailSender;
import com.odeyalo.analog.auth.service.register.mail.SimpleMicroserviceDelegateMailSender;
import com.odeyalo.analog.auth.service.support.generatators.CodeGenerator;
import com.odeyalo.analog.auth.service.support.generatators.DigitCodeGenerator;
import com.odeyalo.analog.auth.service.support.verification.EmailCodeVerificationManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class VerificationCodeMailSenderFacadeImplUnitTest {
    MailSender mailSender;
    VerificationCodeRepository verificationCodeRepository;
    CodeGenerator generator;
    VerificationCodeMailSenderFacadeImpl verificationCodeMailSenderFacade;

    public static final Integer VERIFICATION_CODE_ID = 10;
    public static final String VERIFICATION_CODE_TEXT_VALUE = "123456";

    @BeforeAll
    void beforeAll() {
        this.mailSender = Mockito.mock(SimpleMicroserviceDelegateMailSender.class);
        this.verificationCodeRepository = Mockito.mock(VerificationCodeRepository.class);
        this.generator = Mockito.mock(DigitCodeGenerator.class);
        this.verificationCodeMailSenderFacade = new VerificationCodeMailSenderFacadeImpl(mailSender, new EmailCodeVerificationManager(verificationCodeRepository, generator));
    }


    @BeforeEach
    void setUp() {
        doAnswer(mock -> {
            VerificationCode verificationCode = (VerificationCode) mock.getArguments()[0];
            verificationCode.setId(VERIFICATION_CODE_ID);
            return verificationCode;
        }).when(verificationCodeRepository).save(any(VerificationCode.class));

        Mockito.when(generator.code(CodeGenerator.DEFAULT_CODE_LENGTH)).thenReturn(VERIFICATION_CODE_TEXT_VALUE);
    }

    @Test
    void generateAndSend() {
        this.verificationCodeMailSenderFacade.generateAndSend(User.builder()
                .id(1)
                .email("email@gmail.com")
                .password("123")
                .activated(true)
                .banned(false)
                .role(Role.USER)
                .nickname("nick123")
                .authProvider(AuthProvider.LOCAL)
                .build(), CodeGenerator.DEFAULT_CODE_LENGTH);
        verify(mailSender, times(1)).send(anyString(), anyString(), anyString());
        verify(generator, times(1)).code(CodeGenerator.DEFAULT_CODE_LENGTH);
        verify(verificationCodeRepository, times(1)).save(any(VerificationCode.class));
    }
}
