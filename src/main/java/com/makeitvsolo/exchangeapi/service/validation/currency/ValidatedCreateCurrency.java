package com.makeitvsolo.exchangeapi.service.validation.currency;

import com.makeitvsolo.exchangeapi.core.validation.ValidatedBefore;
import com.makeitvsolo.exchangeapi.service.dto.currency.CreateCurrencyDto;
import com.makeitvsolo.exchangeapi.service.dto.currency.CurrencyCodeDto;

public final class ValidatedCreateCurrency implements ValidatedBefore<CreateCurrencyDto> {
    private final ValidatedBefore<CurrencyCodeDto> code;
    private final ValidatedBefore<String> fullName;
    private final ValidatedBefore<String> sign;

    ValidatedCreateCurrency(
            ValidatedBefore<CurrencyCodeDto> code, ValidatedBefore<String> fullName, ValidatedBefore<String> sign
    ) {
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }


    public static ValidatedCreateCurrency of(String code, String fullName, String sign) {
        return new ValidatedCreateCurrency(
                new ValidatedCode(code),
                new ValidatedName(fullName),
                new ValidatedSign(sign)
        );
    }

    @Override
    public CreateCurrencyDto validated() {
        return new CreateCurrencyDto(code.validated().value(), fullName.validated(), sign.validated());
    }
}
