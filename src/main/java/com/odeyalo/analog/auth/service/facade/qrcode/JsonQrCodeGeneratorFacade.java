package com.odeyalo.analog.auth.service.facade.qrcode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.WriterException;
import com.odeyalo.analog.auth.dto.response.QrCodeDTO;
import com.odeyalo.analog.auth.entity.QrCode;
import com.odeyalo.analog.auth.repository.QrCodeRepository;
import com.odeyalo.analog.auth.service.support.generatators.CodeGenerator;
import com.odeyalo.analog.auth.service.support.generatators.QrCodeGenerator;
import com.odeyalo.analog.auth.service.support.generatators.SimpleQrCodeGenerator;
import io.swagger.v3.core.util.ObjectMapperFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Generate qr code instance, save qr code to database and return path to generated qr code
 */
@Component
public class JsonQrCodeGeneratorFacade implements QrCodeGeneratorFacade {
    private final QrCodeGenerator qrCodeGenerator;
    private final CodeGenerator codeGenerator;
    private final QrCodeRepository qrCodeRepository;
    private final ObjectMapper mapper = ObjectMapperFactory.buildStrictGenericObjectMapper();
    private static final String CODE_REQUEST_PARAM = "?code=";
    @Value("${qr.code.verify.url}")
    private String QR_CODE_VERIFY_URL;

    public JsonQrCodeGeneratorFacade(QrCodeGenerator qrCodeGenerator,
                                     @Qualifier("qrIdCodeGenerator") CodeGenerator codeGenerator, QrCodeRepository qrCodeRepository) {
        this.qrCodeGenerator = qrCodeGenerator;
        this.codeGenerator = codeGenerator;
        this.qrCodeRepository = qrCodeRepository;
    }

    @Override
    @Transactional
    public String generateQrCode(String clientId, Integer width, Integer height) throws IOException, WriterException {
        String code = this.codeGenerator.code(20);
        String redirectUrl = QR_CODE_VERIFY_URL + CODE_REQUEST_PARAM + code;
        String time = LocalDateTime.now().toString();
        String value = this.buildData(clientId, redirectUrl, time);
        QrCode qrCode = QrCode.builder()
                .clientId(clientId)
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .isActivated(false)
                .qrCodeValue(value)
                .build();
        this.qrCodeRepository.deleteByClientId(clientId);
        this.qrCodeRepository.save(qrCode);
        return this.qrCodeGenerator.generateQrCode(width, height, value, SimpleQrCodeGenerator.DEFAULT_FILE_PATH);
    }

    private String buildData(String clientId, String url, String time) throws JsonProcessingException {
        QrCodeDTO dto = new QrCodeDTO(clientId, url, time);
        return this.mapper.writeValueAsString(dto);
    }
}
