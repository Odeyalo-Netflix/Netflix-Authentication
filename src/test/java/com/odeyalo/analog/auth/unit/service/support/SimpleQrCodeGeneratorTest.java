package com.odeyalo.analog.auth.unit.service.support;

import com.google.zxing.WriterException;
import com.odeyalo.analog.auth.service.support.generatators.QrCodeGenerator;
import com.odeyalo.analog.auth.service.support.generatators.SimpleQrCodeGenerator;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleQrCodeGeneratorTest {
    SimpleQrCodeGenerator generateQrCode = new SimpleQrCodeGenerator();
    private static final  String CURRENT_PATH = Paths.get(".").toAbsolutePath().normalize().toString();
    private static final  String TEST_DIRECTORY_PATH = CURRENT_PATH + "\\src\\test\\resources\\login\\qrcode\\";

    @BeforeAll
    static void createTestFolder() throws IOException {
        new File(TEST_DIRECTORY_PATH).mkdirs();
    }
    @Test
    void generateQrCode() throws IOException, WriterException {
        String qrCodePath = this.generateQrCode.generateQrCode(QrCodeGenerator.DEFAULT_WIDTH, QrCodeGenerator.DEFAULT_HEIGHT, "HELLO!", TEST_DIRECTORY_PATH);
        assertTrue(FileUtils.directoryContains(new File(TEST_DIRECTORY_PATH), new File(qrCodePath)));
    }

    @AfterAll
    static void deleteAll() throws IOException {
       FileUtils.deleteDirectory(new File(TEST_DIRECTORY_PATH));
    }
}
