package com.odeyalo.analog.auth.support;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RsaTokenPairFileWriter implements FileWriter {
    private static final String RSA_KEY_EXTENSION = "key";

    @Override
    public void write(Path path, byte[] data) throws IOException {
//        String extension = FilenameUtils.getExtension(path.getFileName().toString());
//        if (!RSA_KEY_EXTENSION.equals(extension))
//            throw new IOException(String.format("To write rsa token to file, file must have extension: %s", RSA_KEY_EXTENSION));
        Files.write(path, data);
    }
}
