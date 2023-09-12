package com.makeitvsolo.exchangeapi.service.dto.exchange;

import com.makeitvsolo.exchangeapi.service.dto.currency.CurrencyDto;

import java.math.BigDecimal;

public record ExchangeDto(CurrencyDto base, CurrencyDto target, BigDecimal rate) {
}
