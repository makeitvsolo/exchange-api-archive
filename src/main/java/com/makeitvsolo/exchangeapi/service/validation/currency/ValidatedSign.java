package com.makeitvsolo.exchangeapi.service.validation.currency;

import com.makeitvsolo.exchangeapi.core.validation.ValidatedBefore;
import com.makeitvsolo.exchangeapi.service.exception.validation.InvalidPayloadException;

public final class ValidatedSign implements ValidatedBefore<String> {
    private final String sign;

    public ValidatedSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String validated() {
        if (sign == null) {
            throw new InvalidPayloadException("Currency sign is null");
        }

        if (sign.isBlank()) {
            throw new InvalidPayloadException("Currency sign should be not empty");
        }

        return sign;
    }
}
