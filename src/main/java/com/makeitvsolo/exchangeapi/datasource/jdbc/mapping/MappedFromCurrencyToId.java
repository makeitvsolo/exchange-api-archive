package com.makeitvsolo.exchangeapi.datasource.jdbc.mapping;

import com.makeitvsolo.exchangeapi.domain.mapping.MappedFromCurrency;

import java.util.UUID;

public final class MappedFromCurrencyToId implements MappedFromCurrency<UUID> {

    public MappedFromCurrencyToId() {
    }

    @Override
    public UUID from(UUID id, String code, String fullName, String sign) {
        return id;
    }
}
