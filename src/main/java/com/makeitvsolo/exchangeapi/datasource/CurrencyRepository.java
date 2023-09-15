package com.makeitvsolo.exchangeapi.datasource;

import com.makeitvsolo.exchangeapi.domain.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyRepository {

    void save(Currency currency);

    List<Currency> fetchAll();
    Optional<Currency> fetchByCode(String code);
}
