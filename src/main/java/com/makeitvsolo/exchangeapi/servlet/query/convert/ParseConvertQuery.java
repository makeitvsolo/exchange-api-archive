package com.makeitvsolo.exchangeapi.servlet.query.convert;

import com.makeitvsolo.exchangeapi.service.dto.convert.ConvertAmountDto;
import com.makeitvsolo.exchangeapi.servlet.exception.ParseQueryException;
import com.makeitvsolo.exchangeapi.servlet.query.ParseQuery;

import java.math.BigDecimal;

public final class ParseConvertQuery implements ParseQuery<ConvertAmountDto> {
    private final ParseQuery<String> codeQuery;
    private final ParseQuery<BigDecimal> amountQuery;

    public ParseConvertQuery(ParseQuery<String> codeQuery, ParseQuery<BigDecimal> amountQuery) {
        this.codeQuery = codeQuery;
        this.amountQuery = amountQuery;
    }

    @Override
    public ConvertAmountDto parse(String query) {
        if (query == null) {
            throw new ParseQueryException("Convert query is null");
        }

        var queryParams = query.split("&");
        if (queryParams.length != 3) {
            throw new ParseQueryException("Convert should contains `from=`, `to=`, `amount=`");
        }

        var from = codeQuery.parse(queryParams[0].replaceAll("from=", ""));
        var to = codeQuery.parse(queryParams[1].replaceAll("to=", ""));
        var amount = amountQuery.parse(queryParams[2].replaceAll("amount=", ""));

        return new ConvertAmountDto(from, to, amount);
    }
}
