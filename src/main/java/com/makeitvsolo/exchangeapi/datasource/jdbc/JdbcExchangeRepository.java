package com.makeitvsolo.exchangeapi.datasource.jdbc;

import com.makeitvsolo.exchangeapi.datasource.ExchangeRepository;
import com.makeitvsolo.exchangeapi.datasource.jdbc.exception.JdbcRepositoryException;
import com.makeitvsolo.exchangeapi.datasource.jdbc.parameter.exchange.InsertExchangeParameters;
import com.makeitvsolo.exchangeapi.datasource.jdbc.parameter.exchange.UpdateExchangeParameters;
import com.makeitvsolo.exchangeapi.domain.Currency;
import com.makeitvsolo.exchangeapi.domain.Exchange;
import com.makeitvsolo.exchangeapi.domain.mapping.MappedFromExchange;
import org.javatuples.Pair;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class JdbcExchangeRepository implements ExchangeRepository {
    private final DataSource source;
    private final MappedFromExchange<InsertExchangeParameters> insertParams;
    private final MappedFromExchange<UpdateExchangeParameters> updateParams;

    public JdbcExchangeRepository(
            DataSource source,
            MappedFromExchange<InsertExchangeParameters> insertParams,
            MappedFromExchange<UpdateExchangeParameters> updateParams
    ) {
        this.source = source;
        this.insertParams = insertParams;
        this.updateParams = updateParams;
    }

    private static class Query {
        static final String Insert = """
                INSERT into exchanges (base_currency_code, target_currency_code, rate)
                VALUES (?, ?, ?)
                """;

        static final String Update = """
                UPDATE exchanges
                SET rate = ?
                WHERE base_currency_code = ? AND target_currency_code = ?
                """;

        static final String FetchByCode = """
                SELECT
                    b.code AS base_code,
                    b.full_name AS base_full_name,
                    b.sign AS base_sign,
                    t.code AS target_code,
                    t.full_name AS target_full_name,
                    t.sign AS target_sign,
                    e.rate AS exchange_rate
                FROM exchanges e
                INNER JOIN (
                    SELECT code, full_name, sign
                    FROM currencies
                    WHERE code = ?
                ) b ON e.base_currency_code = b.code
                INNER JOIN (
                    SELECT code, full_name, sign
                    FROM currencies
                    WHERE code = ?
                ) t ON e.target_currency_code = t.code
                """;

        static final String FetchAll = """
                SELECT
                    b.code AS base_code,
                    b.full_name AS base_full_name,
                    b.sign AS base_sign,
                    t.code AS target_code,
                    t.full_name AS target_full_name,
                    t.sign AS target_sign,
                    e.rate AS exchange_rate
                FROM exchanges e
                INNER JOIN currencies b ON e.base_currency_code = b.code
                INNER JOIN currencies t ON e.target_currency_code = t.code
                """;

        static final String FetchAnyCrossPair = """
                SELECT
                    b.code AS base_code,
                    b.full_name AS base_full_name,
                    b.sign AS base_sign,
                    t.code AS target_code,
                    t.full_name AS target_full_name,
                    t.sign AS target_sign,
                    c.rate AS exchange_rate
                FROM (
                    SELECT base_currency_code, target_currency_code, rate
                    FROM (
                        SELECT base_currency_code, target_currency_code, rate
                        FROM exchanges
                        WHERE base_currency_code in (
                            SELECT base_currency_code
                            FROM exchanges
                            WHERE target_currency_code = ?
                            INTERSECT
                            SELECT base_currency_code
                            FROM exchanges
                            WHERE target_currency_code = ?
                            LIMIT 1
                        )
                        UNION
                        SELECT base_currency_code, target_currency_code, rate
                        FROM exchanges
                        WHERE target_currency_code in (
                            SELECT target_currency_code
                            FROM exchanges
                            WHERE base_currency_code = ?
                            INTERSECT
                            SELECT target_currency_code
                            FROM exchanges
                            WHERE base_currency_code = ?
                            LIMIT 1
                        )
                    ) a
                    ORDER BY base_currency_code, target_currency_code
                    LIMIT 2
                ) c
                INNER JOIN currencies b ON c.base_currency_code = b.code
                INNER JOIN currencies t ON c.target_currency_code = t.code
                """;
    }

    @Override
    public void save(Exchange exchange) {
        try (
                var connection = source.getConnection();
                var statement = connection.prepareStatement(Query.Insert)
        ) {
            connection.setAutoCommit(false);

            var params = exchange.map(insertParams);
            statement.setString(1, params.baseCode());
            statement.setString(2, params.targetCode());
            statement.setBigDecimal(3, params.rate());

            statement.execute();

            connection.commit();
        } catch (SQLException e) {
            throw new JdbcRepositoryException(e);
        }
    }

    @Override
    public void update(Exchange exchange) {
        try (
                var connection = source.getConnection();
                var statement = connection.prepareStatement(Query.Update)
        ) {
            connection.setAutoCommit(false);

            var params = exchange.map(updateParams);
            statement.setBigDecimal(1, params.rate());
            statement.setString(2, params.baseCode());
            statement.setString(3, params.targetCode());

            statement.execute();

            connection.commit();
        } catch (SQLException e) {
            throw new JdbcRepositoryException(e);
        }
    }

    @Override
    public Optional<Exchange> fetchByCode(String base, String target) {
        try (
                var connection = source.getConnection();
                var statement = connection.prepareStatement(Query.FetchByCode)
        ) {
            statement.setString(1, base);
            statement.setString(2, target);

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

    @Override
    public List<Exchange> fetchAll() {
        try (
                var connection = source.getConnection();
                var statement = connection.prepareStatement(Query.FetchAll)
        ) {
            statement.execute();
            var cursor = statement.getResultSet();

            List<Exchange> exchanges = new ArrayList<>();
            while (cursor.next()) {
                exchanges.add(nextFrom(cursor));
            }

            return exchanges;
        } catch (SQLException e) {
            throw new JdbcRepositoryException(e);
        }
    }

    @Override
    public Optional<Pair<Exchange, Exchange>> fetchAnyCrossPair(String base, String target) {
        try (
                var connection = source.getConnection();
                var statement = connection.prepareStatement(Query.FetchAnyCrossPair)
        ) {
            statement.setString(1, base);
            statement.setString(2, target);
            statement.setString(3, base);
            statement.setString(4, target);

            statement.execute();
            var cursor = statement.getResultSet();

            if (cursor.next()) {
                var left = nextFrom(cursor);
                cursor.next();
                var right = nextFrom(cursor);

                return Optional.of(new Pair<>(left, right));
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new JdbcRepositoryException(e);
        }
    }

    private Exchange nextFrom(ResultSet cursor) throws SQLException {
        return Exchange.from(
                Currency.from(
                        cursor.getString("base_code"),
                        cursor.getString("base_full_name"),
                        cursor.getString("base_sign")
                ),

                Currency.from(
                        cursor.getString("target_code"),
                        cursor.getString("target_full_name"),
                        cursor.getString("target_sign")
                ),

                cursor.getBigDecimal("exchange_rate")
        );
    }
}
