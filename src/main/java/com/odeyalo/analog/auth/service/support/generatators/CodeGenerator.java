package com.odeyalo.analog.auth.service.support.generatators;

public interface CodeGenerator {
    Integer DEFAULT_CODE_LENGTH = 6;

    String code(int length);

}
