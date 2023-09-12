package com.makeitvsolo.exchangeapi.domain.mapping;

import com.makeitvsolo.exchangeapi.core.mapping.Mapper;

import java.util.UUID;

public interface MappedFromCurrency<R> extends Mapper {

    R from(UUID id, String code, String fullName, String sign);
}
