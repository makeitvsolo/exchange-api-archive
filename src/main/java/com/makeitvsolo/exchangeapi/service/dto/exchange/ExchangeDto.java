package com.makeitvsolo.exchangeapi.service.dto.exchange;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.makeitvsolo.exchangeapi.service.dto.currency.CurrencyDto;

import java.math.BigDecimal;
import java.math.RoundingMode;

@JsonSerialize
@JsonDeserialize
public record ExchangeDto(CurrencyDto base, CurrencyDto target, BigDecimal rate) {

    public ExchangeDto {
        rate = rate.setScale(4, RoundingMode.HALF_EVEN);
    }
}
