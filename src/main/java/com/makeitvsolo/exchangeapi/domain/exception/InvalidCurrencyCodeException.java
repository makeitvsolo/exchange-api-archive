package com.makeitvsolo.exchangeapi.domain.exception;

import com.makeitvsolo.exchangeapi.core.exception.ExchangeApiException;

import java.text.MessageFormat;

public final class InvalidCurrencyCodeException extends ExchangeApiException {

    public InvalidCurrencyCodeException(String code) {
        super(MessageFormat.format("Given code {0} is not in ISO 4217 format", code));
    }
}
