package com.odeyalo.analog.auth.service.facade.qrcode.parser;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface QrCodeParserHandlerFacade {

    void parseAndVerify(InputStream stream) throws Exception;

    void parseAndVerify(MultipartFile file) throws Exception;

}
