package com.odeyalo.analog.auth.unit;

import com.odeyalo.analog.auth.service.support.generatators.CodeGenerator;
import com.odeyalo.analog.auth.service.support.generatators.DigitCodeGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DigitVerificationCodeGeneratorTest {
    private final DigitCodeGenerator digitCodeGenerator = new DigitCodeGenerator();

    @Test
    void code() {
        String code = this.digitCodeGenerator.code(CodeGenerator.DEFAULT_CODE_LENGTH);
        assertEquals(CodeGenerator.DEFAULT_CODE_LENGTH, code.length());
    }
}
