package com.makeitvsolo.exchangeapi.domain.exchange;

import com.makeitvsolo.exchangeapi.core.exception.ExchangeApiException;

public final class WrongExchangeException extends ExchangeApiException {
    WrongExchangeException(String message) {
        super(message);
    }
}
