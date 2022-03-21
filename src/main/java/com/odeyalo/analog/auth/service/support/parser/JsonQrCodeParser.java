package com.odeyalo.analog.auth.service.support.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.odeyalo.analog.auth.dto.response.QrCodeDTO;
import com.odeyalo.analog.auth.exceptions.QrCodeNotFoundException;
import io.swagger.v3.core.util.ObjectMapperFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Component
public class JsonQrCodeParser implements QrCodeParser {
    private final ObjectMapper mapper = ObjectMapperFactory.buildStrictGenericObjectMapper();

    @Override
    public QrCodeDTO parseQrCode(InputStream stream) throws IOException, QrCodeNotFoundException {
        BufferedImage bufferedImage = ImageIO.read(stream);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return this.mapper.readValue(result.getText(), QrCodeDTO.class);
        } catch (NotFoundException exception) {
            throw new QrCodeNotFoundException("QR code not founded on image");
        }
    }

    @Override
    public QrCodeDTO parseQrCode(MultipartFile file) throws IOException, QrCodeNotFoundException {
        return this.parseQrCode(file.getInputStream());
    }
}
