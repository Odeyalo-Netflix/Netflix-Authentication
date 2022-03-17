package com.odeyalo.analog.auth.unit;

import com.odeyalo.analog.auth.service.support.generatators.CodeGenerator;
import com.odeyalo.analog.auth.service.support.generatators.DigitCodeGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DigitVerificationCodeGeneratorTest {
    private final DigitCodeGenerator digitCodeGenerator = new DigitCodeGenerator();

    @Test
    void code() {
        String code = this.digitCodeGenerator.code(CodeGenerator.DEFAULT_CODE_LENGTH);
        assertTrue(Integer.parseInt(code) >= 100_000);
        assertTrue(Integer.parseInt(code) < 1_000_000);
    }
}
