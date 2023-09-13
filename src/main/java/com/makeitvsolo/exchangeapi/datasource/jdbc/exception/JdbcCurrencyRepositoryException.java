package com.makeitvsolo.exchangeapi.datasource.jdbc.exception;

import com.makeitvsolo.exchangeapi.core.exception.ExchangeApiException;

import java.sql.SQLException;

public final class JdbcCurrencyRepositoryException extends ExchangeApiException {

    public JdbcCurrencyRepositoryException(SQLException e) {
        super(e.getMessage());
    }
}
