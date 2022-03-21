package com.odeyalo.analog.auth.service.facade.qrcode;

import com.google.zxing.WriterException;

import java.io.IOException;

/**
 * Generate text and create qr code with this data
 */
public interface QrCodeGeneratorFacade {

    String generateQrCode(String clientId, Integer width, Integer height) throws IOException, WriterException;
}
