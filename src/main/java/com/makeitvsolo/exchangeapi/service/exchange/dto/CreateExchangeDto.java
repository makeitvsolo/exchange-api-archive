package com.makeitvsolo.exchangeapi.service.exchange.dto;

import java.math.BigDecimal;

public record CreateExchangeDto(String base, String target, BigDecimal rate) {
}
