package com.makeitvsolo.exchangeapi.service.exception.validation;

import com.makeitvsolo.exchangeapi.core.exception.ExchangeApiException;

public final class InvalidPayloadException extends ExchangeApiException {

    public InvalidPayloadException(String message) {
        super(message);
    }
}
