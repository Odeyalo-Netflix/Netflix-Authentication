package com.odeyalo.analog.auth.support;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface FileWriter {
    /**
     * Writes data to file
     * @param path - path to file
     * @param data - data in bytes that will be written
     */
    void write(Path path, byte[] data) throws IOException;
}
