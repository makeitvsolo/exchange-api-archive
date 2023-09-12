package com.makeitvsolo.exchangeapi.service.currency.dto;

import java.util.UUID;

public record CurrencyDto(UUID id, String code, String fullName, String sign) {
}
