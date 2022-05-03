package com.odeyalo.analog.auth.service.support.generatators;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DigitCodeGenerator implements CodeGenerator {

    @Override
    public String code(int length) {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }
}
