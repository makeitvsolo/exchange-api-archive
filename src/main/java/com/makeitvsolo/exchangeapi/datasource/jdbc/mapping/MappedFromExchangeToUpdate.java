package com.makeitvsolo.exchangeapi.datasource.jdbc.mapping;

import com.makeitvsolo.exchangeapi.datasource.jdbc.parameter.exchange.UpdateExchangeParameters;
import com.makeitvsolo.exchangeapi.domain.Currency;
import com.makeitvsolo.exchangeapi.domain.mapping.MappedFromCurrency;
import com.makeitvsolo.exchangeapi.domain.mapping.MappedFromExchange;

import java.math.BigDecimal;
import java.util.UUID;

public final class MappedFromExchangeToUpdate implements MappedFromExchange<UpdateExchangeParameters> {
    private final MappedFromCurrency<UUID> mapper;

    public MappedFromExchangeToUpdate(MappedFromCurrency<UUID> mapper) {
        this.mapper = mapper;
    }

    @Override
    public UpdateExchangeParameters from(Currency base, Currency target, BigDecimal rate) {
        return new UpdateExchangeParameters(base.map(mapper).toString(), target.map(mapper).toString(), rate);
    }
}
