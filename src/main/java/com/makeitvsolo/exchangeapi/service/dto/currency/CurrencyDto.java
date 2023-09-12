package com.makeitvsolo.exchangeapi.service.dto.currency;

import java.util.UUID;

public record CurrencyDto(UUID id, String code, String fullName, String sign) {
}
