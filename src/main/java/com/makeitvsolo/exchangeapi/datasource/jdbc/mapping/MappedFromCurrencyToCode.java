package com.makeitvsolo.exchangeapi.datasource.jdbc.mapping;

import com.makeitvsolo.exchangeapi.domain.mapping.MappedFromCurrency;

import java.util.UUID;

public final class MappedFromCurrencyToCode implements MappedFromCurrency<String> {

    public MappedFromCurrencyToCode() {
    }

    @Override
    public String from(String code, String fullName, String sign) {
        return code;
    }
}
