package com.makeitvsolo.exchangeapi.datasource.jdbc.parameter.exchange;

import java.math.BigDecimal;

public record UpdateExchangeParameters(String baseId, String targetId, BigDecimal rate) {
}
