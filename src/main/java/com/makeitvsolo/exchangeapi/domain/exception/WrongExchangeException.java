package com.makeitvsolo.exchangeapi.domain.exception;

import com.makeitvsolo.exchangeapi.core.exception.ExchangeApiException;

public final class WrongExchangeException extends ExchangeApiException {
    public WrongExchangeException(String message) {
        super(message);
    }
}
