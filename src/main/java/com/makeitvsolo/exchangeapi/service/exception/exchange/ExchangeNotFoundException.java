package com.makeitvsolo.exchangeapi.service.exception.exchange;

import com.makeitvsolo.exchangeapi.core.exception.ExchangeApiException;

import java.text.MessageFormat;

public final class ExchangeNotFoundException extends ExchangeApiException {

    public ExchangeNotFoundException(String base, String target) {
        super(MessageFormat.format("Exchange with base {0} and target {1} not found", base, target));
    }
}
