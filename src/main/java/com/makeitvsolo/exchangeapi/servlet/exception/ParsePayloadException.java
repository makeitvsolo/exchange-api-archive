package com.makeitvsolo.exchangeapi.servlet.exception;

import com.makeitvsolo.exchangeapi.core.exception.ExchangeApiException;

public final class ParsePayloadException extends ExchangeApiException {

    public ParsePayloadException(String message) {
        super(message);
    }
}
