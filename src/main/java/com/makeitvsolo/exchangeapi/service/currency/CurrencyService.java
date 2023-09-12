package com.makeitvsolo.exchangeapi.service.currency;

import com.makeitvsolo.exchangeapi.core.unique.Unique;
import com.makeitvsolo.exchangeapi.datasource.currency.CurrencyRepository;
import com.makeitvsolo.exchangeapi.domain.currency.Currency;
import com.makeitvsolo.exchangeapi.domain.currency.MappedFromCurrency;
import com.makeitvsolo.exchangeapi.service.currency.dto.CreateCurrencyDto;
import com.makeitvsolo.exchangeapi.service.currency.dto.CurrencyDto;
import com.makeitvsolo.exchangeapi.service.currency.dto.CurrencyListDto;
import com.makeitvsolo.exchangeapi.service.currency.exception.CurrencyNotFoundException;

import java.util.UUID;

public final class CurrencyService {
    private final CurrencyRepository repository;
    private final Unique<UUID> currencyId;
    private final MappedFromCurrency<CurrencyDto> mapper;

    public CurrencyService(
            CurrencyRepository repository, Unique<UUID> currencyId, MappedFromCurrency<CurrencyDto> mapper
    ) {
        this.repository = repository;
        this.currencyId = currencyId;
        this.mapper = mapper;
    }

    public void create(CreateCurrencyDto payload) {
        var currency = Currency.create(currencyId, payload.code(), payload.fullName(), payload.sign());

        repository.save(currency);
    }

    public CurrencyDto byCode(String code) {
        return repository.fetchByCode(code)
                       .map(currency -> currency.map(mapper))
                       .orElseThrow(() -> new CurrencyNotFoundException(code));
    }

    public CurrencyListDto all() {
        var currencies = repository.fetchAll()
                                 .stream()
                                 .map(currency -> currency.map(mapper))
                                 .toList();

        return new CurrencyListDto(currencies);
    }
}
