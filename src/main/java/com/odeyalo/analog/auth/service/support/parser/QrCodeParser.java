package com.odeyalo.analog.auth.service.support.parser;

import com.google.zxing.NotFoundException;
import com.odeyalo.analog.auth.dto.response.QrCodeDTO;
import com.odeyalo.analog.auth.exceptions.QrCodeNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface QrCodeParser {

    QrCodeDTO parseQrCode(InputStream stream) throws IOException, QrCodeNotFoundException;

    QrCodeDTO parseQrCode(MultipartFile file) throws IOException, QrCodeNotFoundException;


}
