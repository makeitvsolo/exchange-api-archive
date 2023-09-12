package com.makeitvsolo.exchangeapi.datasource.currency;

import com.makeitvsolo.exchangeapi.domain.currency.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyRepository {

    void save(Currency currency);

    List<Currency> fetchAll();
    Optional<Currency> fetchByCode(String code);
}
