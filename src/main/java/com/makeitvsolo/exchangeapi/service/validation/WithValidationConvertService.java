package com.makeitvsolo.exchangeapi.service.validation;

import com.makeitvsolo.exchangeapi.service.ConvertService;
import com.makeitvsolo.exchangeapi.service.dto.convert.ConvertAmountDto;
import com.makeitvsolo.exchangeapi.service.dto.convert.ConvertedAmountDto;
import com.makeitvsolo.exchangeapi.service.validation.convert.ValidatedConvertAmount;

public final class WithValidationConvertService implements ConvertService {
    private final ConvertService base;

    public WithValidationConvertService(ConvertService base) {
        this.base = base;
    }

    @Override
    public ConvertedAmountDto convert(ConvertAmountDto payload) {
        var payloadBefore = ValidatedConvertAmount.of(payload);

        return base.convert(payloadBefore.validated());
    }
}
