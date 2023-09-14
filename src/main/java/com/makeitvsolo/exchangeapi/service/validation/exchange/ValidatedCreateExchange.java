package com.makeitvsolo.exchangeapi.service.validation.exchange;

import com.makeitvsolo.exchangeapi.core.validation.ValidatedBefore;
import com.makeitvsolo.exchangeapi.service.dto.exchange.CreateExchangeDto;
import com.makeitvsolo.exchangeapi.service.validation.currency.ValidatedCode;

import java.math.BigDecimal;

public final class ValidatedCreateExchange implements ValidatedBefore<CreateExchangeDto> {
    private final ValidatedBefore<String> base;
    private final ValidatedBefore<String> target;
    private final BigDecimal rate;

    ValidatedCreateExchange(
            ValidatedBefore<String> base, ValidatedBefore<String> target, BigDecimal rate
    ) {
        this.base = base;
        this.target = target;
        this.rate = rate;
    }

    public static ValidatedCreateExchange of(String base, String target, BigDecimal rate) {
        return new ValidatedCreateExchange(new ValidatedCode(base), new ValidatedCode(target), rate);
    }

    @Override
    public CreateExchangeDto validated() {
        return new CreateExchangeDto(base.validated(), target.validated(), rate);
    }
}
