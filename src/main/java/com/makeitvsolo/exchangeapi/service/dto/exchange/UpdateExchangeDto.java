package com.makeitvsolo.exchangeapi.service.dto.exchange;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.math.RoundingMode;

@JsonSerialize
@JsonDeserialize
public record UpdateExchangeDto(String base, String target, BigDecimal rate) {

    public UpdateExchangeDto {
        rate = rate.setScale(4, RoundingMode.HALF_EVEN);
    }
}
