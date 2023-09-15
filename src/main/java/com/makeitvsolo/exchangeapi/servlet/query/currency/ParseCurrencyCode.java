package com.makeitvsolo.exchangeapi.servlet.query.currency;

import com.makeitvsolo.exchangeapi.servlet.exception.ParseQueryException;
import com.makeitvsolo.exchangeapi.servlet.query.ParseQuery;

public final class ParseCurrencyCode implements ParseQuery<String> {
    public ParseCurrencyCode() {
    }

    @Override
    public String parse(String query) {
        if (query == null) {
            throw new ParseQueryException("Currency code is null");
        }

        if (query.isBlank()) {
            throw new ParseQueryException("Currency code is empty");
        }

        if (query.length() != 3 || !query.equals(query.toUpperCase())) {
            throw new ParseQueryException("Currency code should be in upper case and with len=3");
        }

        return query;
    }
}
