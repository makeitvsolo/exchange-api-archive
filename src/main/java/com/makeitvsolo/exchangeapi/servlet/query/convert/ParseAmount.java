package com.makeitvsolo.exchangeapi.servlet.query.convert;

import com.makeitvsolo.exchangeapi.servlet.exception.ParseQueryException;
import com.makeitvsolo.exchangeapi.servlet.query.ParseQuery;

import java.math.BigDecimal;

public final class ParseAmount implements ParseQuery<BigDecimal> {

    public ParseAmount() {
    }

    @Override
    public BigDecimal parse(String query) {
        try {
            return BigDecimal.valueOf(Double.parseDouble(query));
        } catch (NumberFormatException e) {
            throw new ParseQueryException("Amount is not a number");
        }
    }
}
