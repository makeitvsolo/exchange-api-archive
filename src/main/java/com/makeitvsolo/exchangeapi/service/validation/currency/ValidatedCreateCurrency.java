package com.makeitvsolo.exchangeapi.service.validation.currency;

import com.makeitvsolo.exchangeapi.core.validation.ValidatedBefore;
import com.makeitvsolo.exchangeapi.service.dto.currency.CreateCurrencyDto;

public final class ValidatedCreateCurrency implements ValidatedBefore<CreateCurrencyDto> {
    private final ValidatedBefore<String> code;
    private final ValidatedBefore<String> fullName;
    private final ValidatedBefore<String> sign;

    ValidatedCreateCurrency(
            ValidatedBefore<String> code, ValidatedBefore<String> fullName, ValidatedBefore<String> sign
    ) {
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }

    public static ValidatedCreateCurrency from(String code, String fullName, String sign) {
        return new ValidatedCreateCurrency(
                new ValidatedCode(code),
                new ValidatedName(fullName),
                new ValidatedSign(sign)
        );
    }

    public static ValidatedCreateCurrency of(CreateCurrencyDto payload) {
        return ValidatedCreateCurrency.from(payload.code(), payload.fullName(), payload.sign());
    }

    @Override
    public CreateCurrencyDto validated() {
        return new CreateCurrencyDto(code.validated(), fullName.validated(), sign.validated());
    }
}
