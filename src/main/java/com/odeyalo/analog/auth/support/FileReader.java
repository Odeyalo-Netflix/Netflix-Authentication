package com.odeyalo.analog.auth.support;

import com.odeyalo.analog.auth.exceptions.KeyConstructionException;

import java.io.IOException;
import java.nio.file.Path;

public interface FileReader<R> {
    /**
     * Reads specific file and returns value
     * @param path - path to file that could be read
     * @return - read data
     */
    R readFile(Path path) throws IOException, KeyConstructionException;
}
