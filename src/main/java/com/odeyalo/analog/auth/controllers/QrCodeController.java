package com.odeyalo.analog.auth.controllers;

import com.google.zxing.WriterException;
import com.odeyalo.analog.auth.exceptions.FileNotPresentException;
import com.odeyalo.analog.auth.service.facade.qrcode.QrCodeGeneratorFacade;
import com.odeyalo.analog.auth.service.facade.qrcode.parser.QrCodeParserHandlerFacade;
import com.odeyalo.analog.auth.service.support.generatators.QrCodeGenerator;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/qrcode")
@CrossOrigin("*")
public class QrCodeController {
    private final QrCodeGeneratorFacade qrCodeGenerator;
    private final QrCodeParserHandlerFacade parser;

    public QrCodeController(QrCodeGeneratorFacade qrCodeGenerator,
                            @Qualifier("jsonQrCodeParserHandlerFacade") QrCodeParserHandlerFacade parser) {
        this.qrCodeGenerator = qrCodeGenerator;
        this.parser = parser;
    }

    @GetMapping(value = "/generate", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] generateQrcode(@RequestParam String clientId) throws IOException, WriterException {
        String qrCode = this.qrCodeGenerator.generateQrCode(clientId, QrCodeGenerator.DEFAULT_WIDTH, QrCodeGenerator.DEFAULT_HEIGHT);
        return IOUtils.toByteArray(Files.newInputStream(Paths.get(qrCode)));
    }

    @PostMapping(value = "/verify", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> parse(MultipartFile file) throws Exception {
        if(file == null)
            throw new FileNotPresentException("File is required");
        this.parser.parseAndVerify(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
