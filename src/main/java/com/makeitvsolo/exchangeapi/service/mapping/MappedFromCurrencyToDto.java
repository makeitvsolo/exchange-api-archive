package com.makeitvsolo.exchangeapi.service.mapping;

import com.makeitvsolo.exchangeapi.domain.mapping.MappedFromCurrency;
import com.makeitvsolo.exchangeapi.service.dto.currency.CurrencyDto;

public final class MappedFromCurrencyToDto implements MappedFromCurrency<CurrencyDto> {

    public MappedFromCurrencyToDto() {
    }

    @Override
    public CurrencyDto from(String code, String fullName, String sign) {
        return new CurrencyDto(code, fullName, sign);
    }
}
