package com.makeitvsolo.exchangeapi.core.exception;

public abstract class ExchangeApiException extends RuntimeException {

    protected ExchangeApiException(String message) {
        super(message);
    }
}
