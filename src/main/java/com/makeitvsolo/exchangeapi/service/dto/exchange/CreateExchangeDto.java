package com.makeitvsolo.exchangeapi.service.dto.exchange;

import java.math.BigDecimal;

public record CreateExchangeDto(String base, String target, BigDecimal rate) {
}
