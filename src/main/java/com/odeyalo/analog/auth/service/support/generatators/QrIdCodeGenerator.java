package com.odeyalo.analog.auth.service.support.generatators;

import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
public class QrIdCodeGenerator implements CodeGenerator {

    @Override
    public String code(int length) {
        return UUID.randomUUID().toString();
    }
}
