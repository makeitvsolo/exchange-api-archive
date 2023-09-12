package com.makeitvsolo.exchangeapi.service.dto.convert;

import java.math.BigDecimal;

public record ConvertAmountDto(String base, String target, BigDecimal amount) {
}
