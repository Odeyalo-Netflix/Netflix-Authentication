package com.odeyalo.analog.auth.integration.repository;

import com.odeyalo.analog.auth.entity.QrCode;
import com.odeyalo.analog.auth.repository.QrCodeRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QrCodeRepositoryTest {
    @Autowired
    QrCodeRepository qrCodeRepository;
    public static final String QR_CODE_EXISTED_TEXT_VALUE = "123";
    public static final String QR_CODE_EXISTED_CLIENT_ID = "fe0PmOj5vBk";
    public static final String QR_CODE_NOT_EXISTED_TEXT_VALUE = "NOT_EXISTED_VALUE";

    @BeforeAll
    void setUp() {
        QrCode qrCode = QrCode.builder()
                .qrCodeValue(QR_CODE_EXISTED_TEXT_VALUE)
                .isActivated(false)
                .clientId(QR_CODE_EXISTED_CLIENT_ID)
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .build();
        this.qrCodeRepository.save(qrCode);
    }
    @Test
    @DisplayName("Find by existed code value and expect correct values")
    void findByExistedQrCodeValue() {
        Optional<QrCode> codeValue = this.qrCodeRepository.findByQrCodeValue(QR_CODE_EXISTED_TEXT_VALUE);
        assertTrue(codeValue.isPresent());
        QrCode qrCode = codeValue.get();
        assertNotNull(qrCode.getId());
        assertEquals(QR_CODE_EXISTED_CLIENT_ID, qrCode.getClientId());
        assertEquals(QR_CODE_EXISTED_TEXT_VALUE, qrCode.getQrCodeValue());
        assertTrue(qrCode.getExpiryTime().isAfter(LocalDateTime.now()));
    }
    @Test
    @DisplayName("Find by not existed code value and expect null")
    void findByNotExistedQrCodeValue() {
        Optional<QrCode> codeValue = this.qrCodeRepository.findByQrCodeValue(QR_CODE_NOT_EXISTED_TEXT_VALUE);
        assertFalse(codeValue.isPresent());
    }

    @AfterAll
    void afterAll() {
        this.qrCodeRepository.deleteAll();
    }
}
