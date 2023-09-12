package com.makeitvsolo.exchangeapi.service.exception.exchange;

import com.makeitvsolo.exchangeapi.core.exception.ExchangeApiException;

import java.text.MessageFormat;

public final class ExchangeAlreadyExistsException extends ExchangeApiException {

    public ExchangeAlreadyExistsException(String base, String target) {
        super(MessageFormat.format("Exchange with base {0} and target {1} already exists", base, target));
    }
}
