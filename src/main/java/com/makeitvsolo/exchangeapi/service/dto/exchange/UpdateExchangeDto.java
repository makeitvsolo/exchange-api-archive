package com.makeitvsolo.exchangeapi.service.dto.exchange;

import java.math.BigDecimal;

public record UpdateExchangeDto(String base, String target, BigDecimal rate) {
}
