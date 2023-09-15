package com.makeitvsolo.exchangeapi.datasource.jdbc.mapping;

import com.makeitvsolo.exchangeapi.datasource.jdbc.parameter.exchange.InsertExchangeParameters;
import com.makeitvsolo.exchangeapi.domain.Currency;
import com.makeitvsolo.exchangeapi.domain.mapping.MappedFromCurrency;
import com.makeitvsolo.exchangeapi.domain.mapping.MappedFromExchange;

import java.math.BigDecimal;
import java.util.UUID;

public final class MappedFromExchangeToInsert implements MappedFromExchange<InsertExchangeParameters> {
    private final MappedFromCurrency<String> mapper;

    public MappedFromExchangeToInsert(MappedFromCurrency<String> mapper) {
        this.mapper = mapper;
    }

    @Override
    public InsertExchangeParameters from(Currency base, Currency target, BigDecimal rate) {
        return new InsertExchangeParameters(base.map(mapper), target.map(mapper), rate);
    }
}
