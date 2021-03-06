package com.odeyalo.analog.auth.service.support.generatators;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.UUID;

@Component
public class SimpleQrCodeGenerator implements QrCodeGenerator {
    public static String DEFAULT_FILE_PATH;
    private final Logger logger = LoggerFactory.getLogger(SimpleQrCodeGenerator.class);

    @Override
    public String generateQrCode(Integer width, Integer height, String text, String path) throws WriterException, IOException {
        System.out.println("path: " + path);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        String pathToCode = path + UUID.randomUUID().toString() + ".png";
        Path pathToGeneratedQRCode = FileSystems.getDefault().getPath(pathToCode);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", pathToGeneratedQRCode);
        this.logger.info("Generated qrcode, saved to path: {}", pathToGeneratedQRCode.getFileName().toString());
        return pathToCode;
    }
    @Value("${app.qrcode.path}")
    private void setDefaultFilePath(String value) {
        SimpleQrCodeGenerator.DEFAULT_FILE_PATH = value;
    }
}
