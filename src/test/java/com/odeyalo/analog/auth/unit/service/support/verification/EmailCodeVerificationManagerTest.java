package com.odeyalo.analog.auth.unit.service.support.verification;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.VerificationCode;
import com.odeyalo.analog.auth.entity.enums.AuthProvider;
import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.repository.VerificationCodeRepository;
import com.odeyalo.analog.auth.service.support.generatators.CodeGenerator;
import com.odeyalo.analog.auth.service.support.verification.EmailCodeVerificationManager;
import com.odeyalo.analog.auth.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EmailCodeVerificationManagerTest {
    @Mock
    VerificationCodeRepository repository;
    @Mock
    CodeGenerator generator;
    @InjectMocks
    EmailCodeVerificationManager manager;
    private static final String EXISTED_VERIFICATION_CODE_VALUE = "123456";
    private static final String NOT_EXISTED_VERIFICATION_CODE_VALUE = "NOT_EXISTED_VALUE";

    private static final Integer VERIFICATION_CODE_ID = 1;
    private static final Integer USER_ID = 1;

    @BeforeEach
    public void before() {
        User user = TestUtils.buildGeneratedUser(USER_ID);
        VerificationCode verificationCode = TestUtils.buildVerificationCode(EXISTED_VERIFICATION_CODE_VALUE, user, VERIFICATION_CODE_ID);

        doAnswer(invocationOnMock -> {
            VerificationCode argument = (VerificationCode) invocationOnMock.getArguments()[0];
            argument.setId(1);
            return argument;
        }).when(repository).save(Mockito.any(VerificationCode.class));
        Mockito.when(repository.findCodeByCodeValue(EXISTED_VERIFICATION_CODE_VALUE)).thenReturn(Optional.of(verificationCode));
        Mockito.when(repository.findCodeByCodeValue(NOT_EXISTED_VERIFICATION_CODE_VALUE)).thenReturn(Optional.empty());
        Mockito.when(generator.code(6)).thenReturn(EXISTED_VERIFICATION_CODE_VALUE);

        Mockito.when(manager.getVerificationCodeByCodeValue(EXISTED_VERIFICATION_CODE_VALUE)).thenReturn(Optional.of(verificationCode));
        Mockito.when(manager.getVerificationCodeByCodeValue(NOT_EXISTED_VERIFICATION_CODE_VALUE)).thenReturn(Optional.empty());
    }

    @Test
    void generateAndSave() {
        User generatedUser = User.builder()
                .id(USER_ID)
                .email("generated@gmail.com")
                .password("password")
                .role(Role.USER)
                .authProvider(AuthProvider.LOCAL)
                .nickname("generated123")
                .banned(false)
                .image("")
                .build();
        VerificationCode verificationCode = manager.generateAndSave(generatedUser, 6, 6);
        assertEquals(VERIFICATION_CODE_ID, verificationCode.getId());
        assertEquals(EXISTED_VERIFICATION_CODE_VALUE, verificationCode.getCodeValue());
        assertEquals(generatedUser, verificationCode.getUser());
    }

    @Test
    void getExistedVerificationCodeByCodeValue() {
        Optional<VerificationCode> verificationCodeOptional = this.manager.getVerificationCodeByCodeValue(EXISTED_VERIFICATION_CODE_VALUE);
        assertTrue(verificationCodeOptional.isPresent());
        VerificationCode code = verificationCodeOptional.get();
        assertEquals(EXISTED_VERIFICATION_CODE_VALUE, code.getCodeValue());
        assertEquals(VERIFICATION_CODE_ID, code.getId());
        assertEquals(TestUtils.buildGeneratedUser(USER_ID), code.getUser());
    }

    @Test
    void getNotExistedVerificationCodeByCodeValue() {
        Optional<VerificationCode> verificationCodeByCodeValue = this.manager.getVerificationCodeByCodeValue(NOT_EXISTED_VERIFICATION_CODE_VALUE);
        assertFalse(verificationCodeByCodeValue.isPresent());
    }

    @Test
    void verifyExistedCode() {
        boolean b = this.manager.verifyCode(EXISTED_VERIFICATION_CODE_VALUE);
        assertTrue(b);
    }
    @Test
    void verifyNotExistedCode() {
        boolean b = this.manager.verifyCode(NOT_EXISTED_VERIFICATION_CODE_VALUE);
        assertFalse(b);
    }

    @Test
    void deleteCodeByCodeValue() {
        this.manager.deleteCode(EXISTED_VERIFICATION_CODE_VALUE);
        Mockito.verify(this.repository, times(1)).deleteByCodeValue(EXISTED_VERIFICATION_CODE_VALUE);
    }

    @Test
    void testDeleteCode() {
        this.manager.deleteCode(NOT_EXISTED_VERIFICATION_CODE_VALUE);
        Mockito.verify(this.repository, times(1)).deleteByCodeValue(NOT_EXISTED_VERIFICATION_CODE_VALUE);
    }
}
