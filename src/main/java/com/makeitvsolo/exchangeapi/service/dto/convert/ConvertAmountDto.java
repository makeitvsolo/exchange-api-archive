package com.makeitvsolo.exchangeapi.service.dto.convert;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.math.RoundingMode;

@JsonSerialize
@JsonDeserialize
public record ConvertAmountDto(String base, String target, BigDecimal amount) {

    public ConvertAmountDto {
        amount = amount.setScale(2, RoundingMode.HALF_EVEN);
    }
}
