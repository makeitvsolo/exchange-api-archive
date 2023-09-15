package com.makeitvsolo.exchangeapi.service.exception.currency;

import com.makeitvsolo.exchangeapi.core.exception.ExchangeApiException;

import java.text.MessageFormat;

public final class CurrencyAlreadyExistsException extends ExchangeApiException {

    public CurrencyAlreadyExistsException(String code) {
        super(MessageFormat.format("Currency with code {0} already exists", code));
    }
}
