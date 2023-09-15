package com.makeitvsolo.exchangeapi.service.mapping;

import com.makeitvsolo.exchangeapi.domain.Currency;
import com.makeitvsolo.exchangeapi.domain.mapping.MappedFromCurrency;
import com.makeitvsolo.exchangeapi.domain.mapping.MappedFromExchange;
import com.makeitvsolo.exchangeapi.service.dto.currency.CurrencyDto;
import com.makeitvsolo.exchangeapi.service.dto.exchange.ExchangeDto;

import java.math.BigDecimal;

public final class MappedFromExchangeToDto implements MappedFromExchange<ExchangeDto> {
    private final MappedFromCurrency<CurrencyDto> mapper;

    public MappedFromExchangeToDto(MappedFromCurrency<CurrencyDto> mapper) {
        this.mapper = mapper;
    }

    @Override
    public ExchangeDto from(Currency base, Currency target, BigDecimal rate) {
        return new ExchangeDto(base.map(mapper), target.map(mapper), rate);
    }
}
