package com.makeitvsolo.exchangeapi.service.dto.currency;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonDeserialize
public record CurrencyDto(String code, String fullName, String sign) {
}
