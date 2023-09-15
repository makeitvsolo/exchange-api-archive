package com.makeitvsolo.exchangeapi.service.impl;

import com.makeitvsolo.exchangeapi.datasource.ExchangeRepository;
import com.makeitvsolo.exchangeapi.domain.Exchange;
import com.makeitvsolo.exchangeapi.domain.mapping.MappedFromCurrency;
import com.makeitvsolo.exchangeapi.service.ConvertService;
import com.makeitvsolo.exchangeapi.service.dto.convert.ConvertAmountDto;
import com.makeitvsolo.exchangeapi.service.dto.convert.ConvertedAmountDto;
import com.makeitvsolo.exchangeapi.service.dto.currency.CurrencyDto;
import com.makeitvsolo.exchangeapi.service.exception.exchange.ExchangeNotFoundException;

public final class BaseConvertService implements ConvertService {
    private final ExchangeRepository repository;
    private final MappedFromCurrency<CurrencyDto> mapper;

    public BaseConvertService(ExchangeRepository repository, MappedFromCurrency<CurrencyDto> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ConvertedAmountDto convert(ConvertAmountDto payload) {
        var exchange = repository.fetchByCode(payload.base(), payload.target())
                               .or(() -> {
                                   return repository.fetchByCode(payload.target(), payload.base())
                                                  .map(Exchange.Reversed::of);
                               })
                               .or(() -> {
                                   return repository.fetchAnyCrossPair(payload.base(), payload.target())
                                                  .map(pair -> Exchange.Cross.of(pair.getValue0(), pair.getValue1()));
                               })
                               .orElseThrow(() -> new ExchangeNotFoundException(payload.base(), payload.target()));

        return new ConvertedAmountDto(
                exchange.base().map(mapper),
                exchange.target().map(mapper),
                exchange.rate(),
                payload.amount(),
                exchange.convert(payload.amount())
        );
    }
}
