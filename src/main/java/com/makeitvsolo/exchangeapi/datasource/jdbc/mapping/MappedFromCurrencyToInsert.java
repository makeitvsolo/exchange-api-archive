package com.makeitvsolo.exchangeapi.datasource.jdbc.mapping;

import com.makeitvsolo.exchangeapi.datasource.jdbc.parameter.currency.InsertCurrencyParameters;
import com.makeitvsolo.exchangeapi.domain.mapping.MappedFromCurrency;

import java.util.UUID;

public final class MappedFromCurrencyToInsert implements MappedFromCurrency<InsertCurrencyParameters> {

    public MappedFromCurrencyToInsert() {
    }

    @Override
    public InsertCurrencyParameters from(String code, String fullName, String sign) {
        return new InsertCurrencyParameters(code, fullName, sign);
    }
}
