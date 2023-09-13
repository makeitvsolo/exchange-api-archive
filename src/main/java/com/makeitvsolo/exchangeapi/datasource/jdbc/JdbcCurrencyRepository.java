package com.makeitvsolo.exchangeapi.datasource.jdbc;

import com.makeitvsolo.exchangeapi.datasource.CurrencyRepository;
import com.makeitvsolo.exchangeapi.datasource.jdbc.exception.JdbcRepositoryException;
import com.makeitvsolo.exchangeapi.datasource.jdbc.parameter.currency.InsertCurrencyParameters;
import com.makeitvsolo.exchangeapi.domain.Currency;
import com.makeitvsolo.exchangeapi.domain.mapping.MappedFromCurrency;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class JdbcCurrencyRepository implements CurrencyRepository {
    private final DataSource source;
    private final MappedFromCurrency<InsertCurrencyParameters> insertParams;

    public JdbcCurrencyRepository(DataSource source, MappedFromCurrency<InsertCurrencyParameters> insertParams) {
        this.source = source;
        this.insertParams = insertParams;
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

            var params = currency.map(insertParams);
            statement.setString(1, params.id());
            statement.setString(2, params.code());
            statement.setString(3, params.fullName());
            statement.setString(4, params.sign());

            statement.execute();

            connection.commit();
        } catch (SQLException e) {
            throw new JdbcRepositoryException(e);
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
            throw new JdbcRepositoryException(e);
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
                return Optional.of(nextFrom(cursor));
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new JdbcRepositoryException(e);
        }
    }

    private Currency nextFrom(ResultSet cursor) throws SQLException {
        return Currency.from(
                UUID.fromString(cursor.getString("id")),
                cursor.getString("code"),
                cursor.getString("full_name"),
                cursor.getString("sign")
        );
    }
}
