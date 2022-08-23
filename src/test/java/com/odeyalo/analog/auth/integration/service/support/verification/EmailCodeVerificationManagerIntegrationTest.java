package com.odeyalo.analog.auth.integration.service.support.verification;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.VerificationCode;
import com.odeyalo.analog.auth.entity.enums.AuthProvider;
import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.integration.AbstractIntegrationTest;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.repository.VerificationCodeRepository;
import com.odeyalo.analog.auth.service.support.generatators.CodeGenerator;
import com.odeyalo.analog.auth.service.support.verification.CodeVerificationManager;
import com.odeyalo.analog.auth.service.support.verification.EmailCodeVerificationManager;
import com.odeyalo.analog.auth.utils.TestUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class EmailCodeVerificationManagerIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    EmailCodeVerificationManager codeVerificationManager;
    @Autowired
    VerificationCodeRepository verificationCodeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public static final String USER_EMAIL = "user@gmail.com";
    public static final String USER_RAW_PASSWORD = "password";
    public static final String USER_NICKNAME = "nickname";
    public static final String USER_IMAGE = "IMAGE";
    public static final Set<Role> USER_ROLES = Collections.singleton(Role.USER);
    public static final AuthProvider USER_AUTH_PROVIDER = AuthProvider.LOCAL;

    public static final boolean USER_ACTIVATED = true;
    public static final boolean USER_BANNED = false;


    private User expectedUser;

    public static final String VERIFICATION_CODE_MOCK_USER_EMAIL = "mockuser@gmail.com";
    public static final String VERIFICATION_CODE_MOCK_USER_RAW_PASSWORD = "mockPassword";
    public static final String VERIFICATION_CODE_MOCK_USER_NICKNAME = "mockNickname";
    public static final String VERIFICATION_CODE_MOCK_USER_IMAGE = "image";
    public static final AuthProvider VERIFICATION_CODE_MOCK_AUTH_PROVIDER = AuthProvider.LOCAL;
    public static final Role VERIFICATION_CODE_MOCK_USER_ROLE = Role.USER;
    public static final boolean VERIFICATION_CODE_MOCK_USER_BANNED = false;
    public static final boolean VERIFICATION_CODE_MOCK_USER_ACTIVATED = true;

    public static final String EXISTED_CODE_VALUE = "123456";
    public static final String NOT_EXISTED_CODE_VALUE = "<3";

    private User mockVerificationCodeUser;
    private VerificationCode expectedVerificationCode;

    @BeforeEach
    void setup() {
        String password = this.passwordEncoder.encode(USER_RAW_PASSWORD);
        User user = TestUtils.buildUser(USER_EMAIL, USER_NICKNAME, password, USER_BANNED, USER_AUTH_PROVIDER, USER_ACTIVATED , USER_IMAGE, USER_ROLES);
        this.expectedUser = this.userRepository.save(user);
        String verificationCodeUserPassword = this.passwordEncoder.encode(VERIFICATION_CODE_MOCK_USER_RAW_PASSWORD);
        User mockUser = TestUtils.buildUser(
                VERIFICATION_CODE_MOCK_USER_EMAIL,
                VERIFICATION_CODE_MOCK_USER_NICKNAME,
                verificationCodeUserPassword,
                VERIFICATION_CODE_MOCK_USER_BANNED,
                VERIFICATION_CODE_MOCK_AUTH_PROVIDER,
                VERIFICATION_CODE_MOCK_USER_ACTIVATED,
                VERIFICATION_CODE_MOCK_USER_IMAGE,
                VERIFICATION_CODE_MOCK_USER_ROLE
        );
        this.mockVerificationCodeUser = this.userRepository.save(mockUser);
        VerificationCode code = VerificationCode.builder()
                .codeValue(EXISTED_CODE_VALUE)
                .isActivated(false)
                .expired(LocalDateTime.now().plusMinutes(CodeVerificationManager.DEFAULT_ACTIVE_MINUTES))
                .user(mockVerificationCodeUser)
                .build();
        this.expectedVerificationCode = this.verificationCodeRepository.save(code);
    }

    @Test
    @DisplayName("Generate and save verification code, expect user equals and no exceptions")
    void generateAndSave() {
        VerificationCode savedVerificationCode = this.codeVerificationManager.generateAndSave(expectedUser, CodeGenerator.DEFAULT_CODE_LENGTH, CodeVerificationManager.DEFAULT_ACTIVE_MINUTES);
        Integer id = savedVerificationCode.getId();
        Optional<VerificationCode> optionalVerificationCode = this.verificationCodeRepository.findById(id);
        assertTrue(optionalVerificationCode.isPresent());
        VerificationCode verificationCode = optionalVerificationCode.get();
        User user = verificationCode.getUser();
        assertEquals(expectedUser, user);
    }

    @Test
    @DisplayName("Find existed verification code by existed code value")
    void getVerificationCodeByExistedCodeValue() {
        Optional<VerificationCode> codeOptional = this.codeVerificationManager.getVerificationCodeByCodeValue(EXISTED_CODE_VALUE);
        assertTrue(codeOptional.isPresent());
        VerificationCode verificationCode = codeOptional.get();
        User user = verificationCode.getUser();
        String codeValue = verificationCode.getCodeValue();
        assertEquals(mockVerificationCodeUser, user);
        assertEquals(EXISTED_CODE_VALUE, codeValue);
    }

    @Test
    @DisplayName("Find not existed verification code by not existed code value")
    void getVerificationCodeByNotExistedCodeValue() {
        Optional<VerificationCode> codeOptional = this.codeVerificationManager.getVerificationCodeByCodeValue(NOT_EXISTED_CODE_VALUE);
        assertFalse(codeOptional.isPresent());
    }

    @Test
    @DisplayName("Verify existed code and expect true ")
    void verifyExistedCode() {
        boolean result = this.codeVerificationManager.verifyCode(EXISTED_CODE_VALUE);
        assertTrue(result);
    }

    @Test
    @DisplayName("Verify not existed code and expect false")
    void verifyNotExistedCode() {
        boolean result = this.codeVerificationManager.verifyCode(NOT_EXISTED_CODE_VALUE);
        assertFalse(result);
    }

    @Test
    @DisplayName("Delete existed code")
    void deleteExistedCodeByCodeValue() {
        this.codeVerificationManager.deleteCode(EXISTED_CODE_VALUE);
        long count = this.verificationCodeRepository.count();
        assertEquals(0, count);
    }

    @Test
    @DisplayName("Delete not existed code")
    void deleteNotExistedCodeByCodeValue() {
        this.codeVerificationManager.deleteCode(NOT_EXISTED_CODE_VALUE);
        long count = this.verificationCodeRepository.count();
        assertEquals(1, count);
    }

    @Test
    @DisplayName("Delete existed code by verification code object")
    void testDeleteExistedCodeByVerificationCodeObject() {
        this.codeVerificationManager.deleteCode(expectedVerificationCode);
        long count = this.verificationCodeRepository.count();
        assertEquals(0, count);
    }

    @Test
    @DisplayName("Delete not existed code by verification code object")
    void testDeleteNotExistedCodeByVerificationCodeObject() {
        this.codeVerificationManager.deleteCode(NOT_EXISTED_CODE_VALUE);
        long count = this.verificationCodeRepository.count();
        assertEquals(1, count);
    }

    @AfterEach
    void clear() {
        this.verificationCodeRepository.deleteAll();
        this.userRepository.deleteAll();
    }
}
