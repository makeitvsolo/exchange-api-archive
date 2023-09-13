package com.makeitvsolo.exchangeapi.datasource.jdbc;

import com.makeitvsolo.exchangeapi.datasource.CurrencyRepository;
import com.makeitvsolo.exchangeapi.datasource.jdbc.exception.JdbcCurrencyRepositoryException;
import com.makeitvsolo.exchangeapi.datasource.jdbc.parameter.InsertCurrencyParameters;
import com.makeitvsolo.exchangeapi.domain.Currency;
import com.makeitvsolo.exchangeapi.domain.mapping.MappedFromCurrency;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class JdbcCurrencyRepository implements CurrencyRepository {
    private final DataSource source;
    private final MappedFromCurrency<InsertCurrencyParameters> mapper;

    public JdbcCurrencyRepository(DataSource source, MappedFromCurrency<InsertCurrencyParameters> mapper) {
        this.source = source;
        this.mapper = mapper;
    }

    private static class Query {
        static final String Insert = """
                INSERT into currencies (id, code, full_name, sign)
                VALUES (?, ?, ?, ?)
                """;

        static final String FetchAll = """
                SELECT id, code, full_name, sign
                FROM currencies
                """;

        static final String FetchByCode = """
                SELECT id, code, full_name, sign
                FROM currencies
                WHERE code = ?
                """;
    }

    @Override
    public void save(Currency currency) {
        try (
                var connection = source.getConnection();
                var statement = connection.prepareStatement(Query.Insert)
        ) {
            connection.setAutoCommit(false);

            var insertParams = currency.map(mapper);
            statement.setString(1, insertParams.id());
            statement.setString(2, insertParams.code());
            statement.setString(3, insertParams.fullName());
            statement.setString(4, insertParams.sign());

            statement.execute();

            connection.commit();
        } catch (SQLException e) {
            throw new JdbcCurrencyRepositoryException(e);
        }
    }

    @Override
    public List<Currency> fetchAll() {
        try (
                var connection = source.getConnection();
                var statement = connection.prepareStatement(Query.FetchAll)
        ) {
            statement.execute();
            var cursor = statement.getResultSet();

            List<Currency> currencies = new ArrayList<>();
            while (cursor.next()) {
                currencies.add(Currency.from(
                        UUID.fromString(cursor.getString("id")),
                        cursor.getString("code"),
                        cursor.getString("full_name"),
                        cursor.getString("sign")
                ));
            }

            return currencies;
        } catch (SQLException e) {
            throw new JdbcCurrencyRepositoryException(e);
        }
    }

    @Override
    public Optional<Currency> fetchByCode(String code) {
        try (
                var connection = source.getConnection();
                var statement = connection.prepareStatement(Query.FetchByCode)
        ) {
            statement.setString(1, code);

            statement.execute();
            var cursor = statement.getResultSet();

            if (cursor.next()) {
                return Optional.of(Currency.from(
                        UUID.fromString(cursor.getString("id")),
                        cursor.getString("code"),
                        cursor.getString("full_name"),
                        cursor.getString("sign")
                ));
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new JdbcCurrencyRepositoryException(e);
        }
    }
}
