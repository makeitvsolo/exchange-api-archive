package com.makeitvsolo.exchangeapi.service.exchange;

import com.makeitvsolo.exchangeapi.datasource.currency.CurrencyRepository;
import com.makeitvsolo.exchangeapi.datasource.exchange.ExchangeRepository;
import com.makeitvsolo.exchangeapi.domain.exchange.Exchange;
import com.makeitvsolo.exchangeapi.domain.exchange.MappedFromExchange;
import com.makeitvsolo.exchangeapi.service.currency.exception.CurrencyNotFoundException;
import com.makeitvsolo.exchangeapi.service.exchange.dto.*;
import com.makeitvsolo.exchangeapi.service.exchange.exception.ExchangeNotFoundException;

public final class ExchangeService {
    private final ExchangeRepository exchangeRepository;
    private final CurrencyRepository currencyRepository;
    private final MappedFromExchange<ExchangeDto> mapper;

    public ExchangeService(
            ExchangeRepository exchangeRepository,
            CurrencyRepository currencyRepository,
            MappedFromExchange<ExchangeDto> mapper
    ) {
        this.exchangeRepository = exchangeRepository;
        this.currencyRepository = currencyRepository;
        this.mapper = mapper;
    }

    public void create(CreateExchangeDto payload) {
        var exchange = Exchange.create(
                currencyRepository.fetchByCode(payload.base())
                        .orElseThrow(() -> new CurrencyNotFoundException(payload.base())),

                currencyRepository.fetchByCode(payload.target())
                        .orElseThrow(() -> new CurrencyNotFoundException(payload.target())),

                payload.rate()
        );

        exchangeRepository.save(exchange);
    }

    public void update(UpdateExchangeDto payload) {
        var exchange = exchangeRepository.fetchByCode(payload.base(), payload.target())
                               .orElseThrow(() -> new ExchangeNotFoundException(payload.base(), payload.target()));

        exchangeRepository.save(exchange.updated(payload.rate()));
    }

    public ExchangeDto byCode(ExchangeCodeDto code) {
        return exchangeRepository.fetchByCode(code.base(), code.target())
                       .map(exchange -> exchange.map(mapper))
                       .orElseThrow(() -> new ExchangeNotFoundException(code.base(), code.target()));
    }

    public ExchangeListDto all() {
        var exchanges = exchangeRepository.fetchAll()
                                .stream()
                                .map(exchange -> exchange.map(mapper))
                                .toList();

        return new ExchangeListDto(exchanges);
    }
}