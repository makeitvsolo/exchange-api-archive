package com.makeitvsolo.exchangeapi.datasource.jdbc.parameter.exchange;

import java.math.BigDecimal;

public record InsertExchangeParameters(String baseId, String targetId, BigDecimal rate) {
}
