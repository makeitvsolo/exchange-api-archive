package com.makeitvsolo.exchangeapi.service.validation.convert;

import com.makeitvsolo.exchangeapi.core.validation.ValidatedBefore;
import com.makeitvsolo.exchangeapi.service.dto.convert.ConvertAmountDto;
import com.makeitvsolo.exchangeapi.service.validation.currency.ValidatedCode;

import java.math.BigDecimal;

public final class ValidatedConvertAmount implements ValidatedBefore<ConvertAmountDto> {
    private final ValidatedBefore<String> base;
    private final ValidatedBefore<String> target;
    private final BigDecimal rate;

    ValidatedConvertAmount(ValidatedBefore<String> base, ValidatedBefore<String> target, BigDecimal rate) {
        this.base = base;
        this.target = target;
        this.rate = rate;
    }

    public static ValidatedConvertAmount from(String base, String target, BigDecimal rate) {
        return new ValidatedConvertAmount(new ValidatedCode(base), new ValidatedCode(target), rate);
    }

    public static ValidatedConvertAmount of(ConvertAmountDto payload) {
        return ValidatedConvertAmount.from(payload.base(), payload.target(), payload.amount());
    }

    @Override
    public ConvertAmountDto validated() {
        return new ConvertAmountDto(base.validated(), target.validated(), rate);
    }
}
