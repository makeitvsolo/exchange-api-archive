package com.makeitvsolo.exchangeapi.service.validation;

import com.makeitvsolo.exchangeapi.service.ExchangeService;
import com.makeitvsolo.exchangeapi.service.dto.exchange.*;
import com.makeitvsolo.exchangeapi.service.validation.exchange.ValidatedCreateExchange;
import com.makeitvsolo.exchangeapi.service.validation.exchange.ValidatedExchangeCode;
import com.makeitvsolo.exchangeapi.service.validation.exchange.ValidatedUpdateExchange;

public final class WithValidationExchangeService implements ExchangeService {
    private final ExchangeService base;

    public WithValidationExchangeService(ExchangeService base) {
        this.base = base;
    }

    @Override
    public ExchangeDto create(CreateExchangeDto payload) {
        var payloadBefore = ValidatedCreateExchange.of(payload);

        return base.create(payloadBefore.validated());
    }

    @Override
    public ExchangeDto update(UpdateExchangeDto payload) {
        var payloadBefore = ValidatedUpdateExchange.of(payload);

        return base.update(payloadBefore.validated());
    }

    @Override
    public ExchangeDto byCode(ExchangeCodeDto code) {
        var codeBefore = ValidatedExchangeCode.of(code);

        return base.byCode(codeBefore.validated());
    }

    @Override
    public ExchangeListDto all() {
        return base.all();
    }
}
