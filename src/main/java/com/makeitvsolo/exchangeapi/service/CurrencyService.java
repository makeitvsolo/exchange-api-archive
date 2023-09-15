package com.makeitvsolo.exchangeapi.service;

import com.makeitvsolo.exchangeapi.service.dto.currency.CreateCurrencyDto;
import com.makeitvsolo.exchangeapi.service.dto.currency.CurrencyDto;
import com.makeitvsolo.exchangeapi.service.dto.currency.CurrencyListDto;

public interface CurrencyService {

    CurrencyDto create(CreateCurrencyDto payload);
    CurrencyDto byCode(String code);
    CurrencyListDto all();
}
