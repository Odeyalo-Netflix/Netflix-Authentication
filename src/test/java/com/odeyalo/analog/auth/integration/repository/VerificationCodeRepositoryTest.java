package com.odeyalo.analog.auth.integration.repository;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.VerificationCode;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.repository.VerificationCodeRepository;
import com.odeyalo.analog.auth.utils.TestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VerificationCodeRepositoryTest {
    @Autowired
    VerificationCodeRepository verificationCodeRepository;
    @Autowired
    UserRepository userRepository;
    private User user;
    public static final String EXISTED_VERIFICATION_CODE_TEXT_VALUE = "123";
    public static final String NOT_EXISTED_VERIFICATION_CODE_TEXT_VALUE = "NOT_EXISTED_VALUE";

    @BeforeEach
    void setUp() {
        this.user = this.userRepository.save(TestUtils.buildGeneratedUser(1));
        VerificationCode verificationCode = VerificationCode.builder()
                .codeValue(EXISTED_VERIFICATION_CODE_TEXT_VALUE)
                .isActivated(false)
                .expired(LocalDateTime.now().plusMinutes(5))
                .user(this.user)
                .build();
        this.verificationCodeRepository.save(verificationCode);
    }
    @Test
    @DisplayName("Find code by existed code value and expect correct body")
    void findExistedCodeByCodeValue() {
        Optional<VerificationCode> op = this.verificationCodeRepository.findCodeByCodeValue(EXISTED_VERIFICATION_CODE_TEXT_VALUE);
        assertTrue(op.isPresent());
        VerificationCode verificationCode = op.get();
        assertEquals(EXISTED_VERIFICATION_CODE_TEXT_VALUE, verificationCode.getCodeValue());
        assertEquals(this.user, verificationCode.getUser());
        assertTrue(verificationCode.getExpired().isAfter(LocalDateTime.now()));
    }
    @Test
    @DisplayName("Find code by not existed code value and expect correct body")
    void findNotExistedCodeByCodeValue() {
        Optional<VerificationCode> op = this.verificationCodeRepository.findCodeByCodeValue(NOT_EXISTED_VERIFICATION_CODE_TEXT_VALUE);
        assertFalse(op.isPresent());
    }

    @Test
    @DisplayName("Delete existed code value")
    void deleteExistedByCodeValue() {
        this.verificationCodeRepository.deleteByCodeValue(EXISTED_VERIFICATION_CODE_TEXT_VALUE);
        assertEquals(0, this.verificationCodeRepository.count());
    }
    @Test
    @DisplayName("Delete not existed code value")
    void deleteNotExistedCodeByCodeValue() {
        this.verificationCodeRepository.deleteByCodeValue(NOT_EXISTED_VERIFICATION_CODE_TEXT_VALUE);
        assertEquals(1, this.verificationCodeRepository.count());
    }

    @AfterEach
    void clear() {
        this.verificationCodeRepository.deleteAll();
        this.userRepository.deleteAll();
    }
}
