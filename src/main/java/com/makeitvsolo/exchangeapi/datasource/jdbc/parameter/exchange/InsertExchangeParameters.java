package com.makeitvsolo.exchangeapi.datasource.jdbc.parameter.exchange;

import java.math.BigDecimal;

public record InsertExchangeParameters(String baseCode, String targetCode, BigDecimal rate) {
}
