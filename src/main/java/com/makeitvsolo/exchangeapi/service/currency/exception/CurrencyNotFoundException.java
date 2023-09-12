package com.makeitvsolo.exchangeapi.service.currency.exception;

import com.makeitvsolo.exchangeapi.core.exception.ExchangeApiException;

import java.text.MessageFormat;

public final class CurrencyNotFoundException extends ExchangeApiException {
    public CurrencyNotFoundException(String code) {
        super(MessageFormat.format("Currency with code {0} not found", code));
    }
}
