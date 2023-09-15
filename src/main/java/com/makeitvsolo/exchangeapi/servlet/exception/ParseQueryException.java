package com.makeitvsolo.exchangeapi.servlet.exception;

import com.makeitvsolo.exchangeapi.core.exception.ExchangeApiException;

public final class ParseQueryException extends ExchangeApiException {

    public ParseQueryException(String message) {
        super(message);
    }
}
