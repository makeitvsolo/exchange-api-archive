package com.makeitvsolo.exchangeapi.service.dto.convert;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;

@JsonSerialize
@JsonDeserialize
public record ConvertAmountDto(String base, String target, BigDecimal amount) {
}
