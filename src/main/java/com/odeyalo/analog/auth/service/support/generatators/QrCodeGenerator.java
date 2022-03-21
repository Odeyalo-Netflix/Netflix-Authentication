package com.odeyalo.analog.auth.service.support.generatators;

import com.google.zxing.WriterException;

import java.io.IOException;

public interface QrCodeGenerator {

    Integer DEFAULT_WIDTH = 256;
    Integer DEFAULT_HEIGHT = 256;

    /**
     * Return path of generated qr code
     * @param width
     * @param height
     * @param data
     * @return
     * @throws WriterException
     * @throws IOException
     */
    String generateQrCode(Integer width, Integer height, String data, String path) throws WriterException, IOException;
}
