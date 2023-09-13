package com.makeitvsolo.exchangeapi.datasource.jdbc.exception;

import com.makeitvsolo.exchangeapi.core.exception.ExchangeApiException;

import java.sql.SQLException;

public final class JdbcRepositoryException extends ExchangeApiException {

    public JdbcRepositoryException(SQLException e) {
        super(e.getMessage());
    }
}
