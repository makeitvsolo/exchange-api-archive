package com.makeitvsolo.exchangeapi.servlet.query.exchange;

import com.makeitvsolo.exchangeapi.service.dto.exchange.ExchangeCodeDto;
import com.makeitvsolo.exchangeapi.servlet.exception.ParseQueryException;
import com.makeitvsolo.exchangeapi.servlet.query.ParseQuery;

public final class ParseExchangeCode implements ParseQuery<ExchangeCodeDto> {

    public ParseExchangeCode() {
    }

    @Override
    public ExchangeCodeDto parse(String query) {
        if (query == null) {
            throw new ParseQueryException("Exchange code is null");
        }

        if (query.isBlank()) {
            throw new ParseQueryException("Exchange code is empty");
        }

        if (query.length() != 6 || !query.equals(query.toUpperCase())) {
            throw new ParseQueryException("Exchange code should be in upper case and contains two currencies codes");
        }

        return new ExchangeCodeDto(query.substring(0, 3), query.substring(3));
    }
}
