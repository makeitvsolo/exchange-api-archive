package com.makeitvsolo.exchangeapi.service.validation.currency;

import com.makeitvsolo.exchangeapi.core.validation.ValidatedBefore;
import com.makeitvsolo.exchangeapi.service.exception.validation.InvalidPayloadException;

public final class ValidatedCode implements ValidatedBefore<String> {
    private final String code;

    public ValidatedCode(String code) {
        this.code = code;
    }

    @Override
    public String validated() {
        if (code == null) {
            throw new InvalidPayloadException("Currency code is null");
        }

        if (code.isBlank()) {
            throw new InvalidPayloadException("Currency code should be not empty");
        }

        return code;
    }
}
