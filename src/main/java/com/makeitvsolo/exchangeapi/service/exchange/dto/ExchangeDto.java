package com.makeitvsolo.exchangeapi.service.exchange.dto;

import com.makeitvsolo.exchangeapi.service.currency.dto.CurrencyDto;

import java.math.BigDecimal;

public record ExchangeDto(CurrencyDto base, CurrencyDto target, BigDecimal rate) {
}
