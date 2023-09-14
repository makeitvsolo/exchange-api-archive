package com.makeitvsolo.exchangeapi.service.validation.exchange;

import com.makeitvsolo.exchangeapi.core.validation.ValidatedBefore;
import com.makeitvsolo.exchangeapi.service.dto.exchange.UpdateExchangeDto;
import com.makeitvsolo.exchangeapi.service.validation.currency.ValidatedCode;

import java.math.BigDecimal;

public final class ValidatedUpdateExchange implements ValidatedBefore<UpdateExchangeDto> {
    private final ValidatedBefore<String> base;
    private final ValidatedBefore<String> target;
    private final BigDecimal rate;

    ValidatedUpdateExchange(
            ValidatedBefore<String> base, ValidatedBefore<String> target, BigDecimal rate
    ) {
        this.base = base;
        this.target = target;
        this.rate = rate;
    }

    public static ValidatedUpdateExchange from(String base, String target, BigDecimal rate) {
        return new ValidatedUpdateExchange(new ValidatedCode(base), new ValidatedCode(target), rate);
    }

    public static ValidatedUpdateExchange of(UpdateExchangeDto payload) {
        return ValidatedUpdateExchange.from(payload.base(), payload.target(), payload.rate());
    }

    @Override
    public UpdateExchangeDto validated() {
        return new UpdateExchangeDto(base.validated(), target.validated(), rate);
    }
}
