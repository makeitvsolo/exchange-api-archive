package com.makeitvsolo.exchangeapi.service;

import com.makeitvsolo.exchangeapi.service.dto.exchange.*;

public interface ExchangeService {

    ExchangeDto create(CreateExchangeDto payload);
    ExchangeDto update(UpdateExchangeDto payload);
    ExchangeDto byCode(ExchangeCodeDto code);
    ExchangeListDto all();
}
