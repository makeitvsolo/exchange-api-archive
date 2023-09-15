package com.makeitvsolo.exchangeapi.service.validation.currency;

import com.makeitvsolo.exchangeapi.core.validation.ValidatedBefore;
import com.makeitvsolo.exchangeapi.service.dto.currency.CurrencyCodeDto;

public final class ValidatedCurrencyCode implements ValidatedBefore<CurrencyCodeDto> {
    private final ValidatedBefore<String> code;

    ValidatedCurrencyCode(ValidatedBefore<String> code) {
        this.code = code;
    }

    public static ValidatedCurrencyCode of(String code) {
        return new ValidatedCurrencyCode(new ValidatedCode(code));
    }

    @Override
    public CurrencyCodeDto validated() {
        return new CurrencyCodeDto(code.validated());
    }
}
