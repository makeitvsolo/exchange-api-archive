package com.makeitvsolo.exchangeapi.service.dto.exchange;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonDeserialize
public record ExchangeCodeDto(String base, String target) {
}
