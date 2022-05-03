package com.odeyalo.analog.auth.unit.service.facade.qrcode;

import com.google.zxing.WriterException;
import com.odeyalo.analog.auth.entity.QrCode;
import com.odeyalo.analog.auth.repository.QrCodeRepository;
import com.odeyalo.analog.auth.service.facade.qrcode.JsonQrCodeGeneratorFacade;
import com.odeyalo.analog.auth.service.support.generatators.CodeGenerator;
import com.odeyalo.analog.auth.service.support.generatators.QrCodeGenerator;
import com.odeyalo.analog.auth.service.support.generatators.SimpleQrCodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class JsonQrCodeGeneratorFacadeTest {
    @Mock
    QrCodeRepository qrCodeRepository;
    @Mock
    CodeGenerator codeGenerator;
    @Mock
    QrCodeGenerator qrCodeGenerator;
    @InjectMocks
    JsonQrCodeGeneratorFacade jsonQRCodeGenerator;

    private static final String CLIENT_ID = "CLIENT_ID: 123";
    private static final String DEFAULT_CODE_GENERATOR_RETURN_VALUE = "123";
    private static final String DEFAULT_GENERATED_QR_CODE_PATH = "PATH_TO_GENERATED_QR_CODES";

    @BeforeEach
    void setUp() throws IOException, WriterException {
        doAnswer(mock -> {
            QrCode qrCode = (QrCode) mock.getArguments()[0];
            qrCode.setId(1);
            return qrCode;
        }).when(qrCodeRepository).save(Mockito.any(QrCode.class));

        Mockito.when(qrCodeGenerator.generateQrCode(
                eq(QrCodeGenerator.DEFAULT_WIDTH), eq(QrCodeGenerator.DEFAULT_HEIGHT),
                anyString(), eq(SimpleQrCodeGenerator.DEFAULT_FILE_PATH)))
                .thenReturn(DEFAULT_GENERATED_QR_CODE_PATH);
        Mockito.when(codeGenerator.code(20)).thenReturn(DEFAULT_CODE_GENERATOR_RETURN_VALUE);
    }

    @Test
    void generateQrCode() throws IOException, WriterException {
        String qrCode = this.jsonQRCodeGenerator.generateQrCode(CLIENT_ID, QrCodeGenerator.DEFAULT_WIDTH, QrCodeGenerator.DEFAULT_HEIGHT);
        assertEquals(DEFAULT_GENERATED_QR_CODE_PATH, qrCode);
    }
}
