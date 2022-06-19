package com.odeyalo.analog.auth.unit.service.support.parser;

import com.odeyalo.analog.auth.dto.response.QrCodeDTO;
import com.odeyalo.analog.auth.exceptions.QrCodeNotFoundException;
import com.odeyalo.analog.auth.service.support.parser.JsonQrCodeParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class JsonQrCodeParserTest {
    private static final String CURRENT_PATH = Paths.get(".").toAbsolutePath().normalize().toString();
    private static final String QR_CODE_IMAGE = new StringBuilder(CURRENT_PATH)
            .append(File.separator)
            .append("src")
            .append(File.separator)
            .append("test")
            .append(File.separator)
            .append("resources")
            .append(File.separator)
            .append("login")
            .append(File.separator)
            .append("test.qrcode.png").toString();
    private static final String IMAGE_WITHOUT_QR_CODE = new StringBuilder(CURRENT_PATH)
            .append(File.separator)
            .append("src")
            .append(File.separator)
            .append("test")
            .append(File.separator)
            .append("resources")
            .append(File.separator)
            .append("login")
            .append(File.separator)
            .append("test.no_qrcode.jpg").toString();
    private final String EXPECTED_URL = "http://localhost:8761/api/v1/qrcode/verify?code=6ccdfe04-adbe-46d6-9c86-c067d2c05ba9";
    private final String EXPECTED_TIME = "2022-03-21T12:13:35.821";
    private final String EXPECTED_CLIENT_ID = "123";

    private final JsonQrCodeParser parser = new JsonQrCodeParser();

    @BeforeAll
    static void setup() {
    }

    @Test
    void parseQrCodeInputStream() throws IOException {
        QrCodeDTO qrCodeDTO = this.parser.parseQrCode(new FileInputStream(QR_CODE_IMAGE));
        assertNotNull(qrCodeDTO);
        assertEquals(EXPECTED_CLIENT_ID, qrCodeDTO.getClientId());
        assertEquals(EXPECTED_URL, qrCodeDTO.getData());
        assertEquals(EXPECTED_TIME, qrCodeDTO.getTime());
    }

    @Test
    void testParseQrCodeMultipartFile() throws IOException {
        QrCodeDTO qrCodeDTO = this.parser.parseQrCode(new MockMultipartFile("Image", new FileInputStream(QR_CODE_IMAGE)));
        assertNotNull(qrCodeDTO);
        assertEquals(EXPECTED_CLIENT_ID, qrCodeDTO.getClientId());
        assertEquals(EXPECTED_URL, qrCodeDTO.getData());
        assertEquals(EXPECTED_TIME, qrCodeDTO.getTime());
    }

    @Test
    void parseQrCodeInputStreamWrongImage() throws IOException {
        assertThrows(QrCodeNotFoundException.class, () -> {
            this.parser.parseQrCode(new FileInputStream(IMAGE_WITHOUT_QR_CODE));
        });
    }

    @Test
    void testParseQrCodeMultipartFileWrongImage() throws IOException {
        assertThrows(QrCodeNotFoundException.class, () -> {
            this.parser.parseQrCode(new MockMultipartFile("Image", new FileInputStream(IMAGE_WITHOUT_QR_CODE)));
        });
    }
}
