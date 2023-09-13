package com.makeitvsolo.exchangeapi.service.dto.convert;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.makeitvsolo.exchangeapi.service.dto.currency.CurrencyDto;

import java.math.BigDecimal;

@JsonSerialize
@JsonDeserialize
public record ConvertedAmountDto(
        CurrencyDto base,
        CurrencyDto target,
        BigDecimal rate,
        BigDecimal amount,
        BigDecimal convertedAmount
) {
}
