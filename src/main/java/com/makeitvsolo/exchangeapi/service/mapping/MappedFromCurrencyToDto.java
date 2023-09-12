package com.makeitvsolo.exchangeapi.service.mapping;

import com.makeitvsolo.exchangeapi.domain.mapping.MappedFromCurrency;
import com.makeitvsolo.exchangeapi.service.dto.currency.CurrencyDto;

import java.util.UUID;

public final class MappedFromCurrencyToDto implements MappedFromCurrency<CurrencyDto> {

    public MappedFromCurrencyToDto() {
    }

    @Override
    public CurrencyDto from(UUID id, String code, String fullName, String sign) {
        return new CurrencyDto(id, code, fullName, sign);
    }
}
