package com.makeitvsolo.exchangeapi.service.validation;

import com.makeitvsolo.exchangeapi.service.CurrencyService;
import com.makeitvsolo.exchangeapi.service.dto.currency.CreateCurrencyDto;
import com.makeitvsolo.exchangeapi.service.dto.currency.CurrencyDto;
import com.makeitvsolo.exchangeapi.service.dto.currency.CurrencyListDto;
import com.makeitvsolo.exchangeapi.service.validation.currency.ValidatedCode;
import com.makeitvsolo.exchangeapi.service.validation.currency.ValidatedCreateCurrency;

public final class WithValidationCurrencyService implements CurrencyService {
    private final CurrencyService base;

    public WithValidationCurrencyService(CurrencyService base) {
        this.base = base;
    }

    @Override
    public CurrencyDto create(CreateCurrencyDto payload) {
        var payloadBefore = ValidatedCreateCurrency.of(payload);

        return base.create(payloadBefore.validated());
    }

    @Override
    public CurrencyDto byCode(String code) {
        var codeBefore = new ValidatedCode(code);

        return base.byCode(codeBefore.validated());
    }

    @Override
    public CurrencyListDto all() {
        return base.all();
    }
}
