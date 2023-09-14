package com.makeitvsolo.exchangeapi.service.validation.exchange;

import com.makeitvsolo.exchangeapi.core.validation.ValidatedBefore;
import com.makeitvsolo.exchangeapi.service.dto.exchange.ExchangeCodeDto;
import com.makeitvsolo.exchangeapi.service.validation.currency.ValidatedCode;

public final class ValidatedExchangeCode implements ValidatedBefore<ExchangeCodeDto> {
    private final ValidatedBefore<String> base;
    private final ValidatedBefore<String> target;

    ValidatedExchangeCode(ValidatedBefore<String> base, ValidatedBefore<String> target) {
        this.base = base;
        this.target = target;
    }

    public static ValidatedExchangeCode of(String base, String target) {
        return new ValidatedExchangeCode(new ValidatedCode(base), new ValidatedCode(target));
    }

    @Override
    public ExchangeCodeDto validated() {
        return new ExchangeCodeDto(base.validated(), target.validated());
    }
}
