package com.odeyalo.analog.auth.service.recovery;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PasswordRecoveryManagerFactory {
    private final Map<PasswordRecoveryType, PasswordRecoveryManager> types;

    public PasswordRecoveryManagerFactory() {
        this.types = new HashMap<>();
    }

    public void registerPasswordRecoveryManager(PasswordRecoveryType type, PasswordRecoveryManager manager) {
        this.types.put(type, manager);
    }

    public PasswordRecoveryManager getManager(PasswordRecoveryType type) {
        return types.get(type);
    }
}
