package com.makeitvsolo.exchangeapi.service.validation.currency;

import com.makeitvsolo.exchangeapi.core.validation.ValidatedBefore;
import com.makeitvsolo.exchangeapi.service.exception.validation.InvalidPayloadException;

public final class ValidatedName implements ValidatedBefore<String> {
    private final String fullName;

    public ValidatedName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String validated() {
        if (fullName == null) {
            throw new InvalidPayloadException("Currency name is null");
        }

        if (fullName.isBlank()) {
            throw new InvalidPayloadException("Currency name should be not empty");
        }

        return fullName;
    }
}
