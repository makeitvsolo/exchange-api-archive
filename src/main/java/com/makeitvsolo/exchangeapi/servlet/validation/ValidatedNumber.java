package com.makeitvsolo.exchangeapi.servlet.validation;

import com.makeitvsolo.exchangeapi.core.validation.ValidatedBefore;
import com.makeitvsolo.exchangeapi.service.exception.validation.InvalidPayloadException;

import java.math.BigDecimal;
import java.text.MessageFormat;

public final class ValidatedNumber implements ValidatedBefore<BigDecimal> {
    private final String stringValue;

    public ValidatedNumber(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public BigDecimal validated() {
        try {
            return BigDecimal.valueOf(Double.parseDouble(stringValue));
        } catch (NumberFormatException e) {
            throw new InvalidPayloadException(MessageFormat.format("{0} is not a number", stringValue));
        } catch (NullPointerException e) {
            throw new InvalidPayloadException("Number is null");
        }
    }
}
