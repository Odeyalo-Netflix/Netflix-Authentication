package com.odeyalo.analog.auth.exceptions;

public class JwtParserConstructionException extends Exception {
    public JwtParserConstructionException(String message) {
        super(message);
    }

    public JwtParserConstructionException(String message, Throwable cause) {
        super(message, cause);
    }
}
