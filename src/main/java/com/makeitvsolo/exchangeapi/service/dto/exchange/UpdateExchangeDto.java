package com.makeitvsolo.exchangeapi.service.dto.exchange;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;

@JsonSerialize
@JsonDeserialize
public record UpdateExchangeDto(String base, String target, BigDecimal rate) {
}
