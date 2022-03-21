package com.odeyalo.analog.auth.service.support.generatators;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.UUID;

@Component
public class SimpleQrCodeGenerator implements QrCodeGenerator {
    private final static String DEFAULT_FILE_PATH = "C:\\Users\\thepr_2iz2cnv\\Desktop\\qrcodes\\";
    private Logger logger = LoggerFactory.getLogger(SimpleQrCodeGenerator.class);

    @Override
    public String generateQrCode(Integer width, Integer height, String text) throws IOException, WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        String pathToCode = DEFAULT_FILE_PATH + UUID.randomUUID().toString() + ".png";
        Path path = FileSystems.getDefault().getPath(pathToCode);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        this.logger.info("Generated qrcode, saved to path: {}", path.getFileName().toString());
        return pathToCode;
    }
}
