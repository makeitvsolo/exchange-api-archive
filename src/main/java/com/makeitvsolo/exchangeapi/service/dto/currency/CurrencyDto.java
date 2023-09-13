package com.makeitvsolo.exchangeapi.service.dto.currency;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.UUID;

@JsonSerialize
@JsonDeserialize
public record CurrencyDto(UUID id, String code, String fullName, String sign) {
}
