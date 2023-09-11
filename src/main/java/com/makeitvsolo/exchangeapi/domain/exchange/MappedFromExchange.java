package com.makeitvsolo.exchangeapi.domain.exchange;

import com.makeitvsolo.exchangeapi.core.mapping.Mapper;
import com.makeitvsolo.exchangeapi.domain.currency.Currency;

import java.math.BigDecimal;

public interface MappedFromExchange<R> extends Mapper {

    R from(Currency base, Currency target, BigDecimal rate);
}
