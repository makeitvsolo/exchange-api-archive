package com.makeitvsolo.exchangeapi.service.dto.currency;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize
@JsonDeserialize
public record CurrencyListDto(List<CurrencyDto> currencies) {
}
