package com.makeitvsolo.exchangeapi.service.dto.convert;

import com.makeitvsolo.exchangeapi.service.dto.currency.CurrencyDto;

import java.math.BigDecimal;

public record ConvertedAmountDto(
        CurrencyDto base,
        CurrencyDto target,
        BigDecimal rate,
        BigDecimal amount,
        BigDecimal convertedAmount
) {
}
