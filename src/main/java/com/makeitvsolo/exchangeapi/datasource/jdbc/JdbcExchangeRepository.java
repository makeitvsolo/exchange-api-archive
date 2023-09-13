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
                INSERT into exchanges (base_currency_id, target_currency_id, rate)
                VALUES (?, ?, ?)
                """;

        static final String Update = """
                UPDATE exchanges
                SET rate = ?
                WHERE base_currency_id = ? AND target_currency_id = ?
                """;

        static final String FetchByCode = """
                SELECT
                    bases.id AS base_id
                    bases.code AS base_code
                    bases.full_name AS base_full_name
                    bases.sign AS base_sign
                    targets.id AS target_id
                    targets.code AS target_code
                    targets.full_name AS target_full_name
                    targets.sign AS target_sign
                    exchanges.rate AS exchange_rate
                FROM exchanges
                INNER JOIN (
                    SELECT id, code, full_name, sign
                    FROM currencies
                    WHERE code = ?
                ) bases ON exchange.base_currency_id = bases.id
                INNER JOIN (
                    SELECT id, code, full_name, sign
                    FROM currencies
                    WHERE code = ?
                ) targets ON exchange.target_currency_id = targets.id
                """;

        static final String FetchAll = """
                SELECT
                    bases.id AS base_id
                    bases.code AS base_code
                    bases.full_name AS base_full_name
                    bases.sign AS base_sign
                    targets.id AS target_id
                    targets.code AS target_code
                    targets.full_name AS target_full_name
                    targets.sign AS target_sign
                    exchanges.rate AS exchange_rate
                FROM exchanges
                INNER JOIN currencies bases ON exchanges.base_currency_id = bases.id
                INNER JOIN currencies targets ON exchanges.target_currency_id = targets.id
                """;

        static final String FetchAnyCrossPair = """
                SELECT
                    bases.id AS base_id
                    bases.code AS base_code
                    bases.full_name AS base_full_name
                    bases.sign AS base_sign
                    targets.id AS target_id
                    targets.code AS target_code
                    targets.full_name AS target_full_name
                    targets.sign AS target_sign
                    cross_exchanges.rate AS exchange_rate
                FROM (
                    SELECT base_currency_id, target_currency_id, rate
                    FROM (
                        SELECT base_currency_id, target_currency_id, rate
                        FROM exchanges
                        WHERE base_currency_id in (
                            SELECT base_currency_id
                            FROM exchanges
                            WHERE target_currency_id = ?
                            INTERSECT
                            SELECT base_currency_id
                            FROM exchanges
                            WHERE target_currency_id = ?
                            LIMIT 1
                        )
                        UNION
                        SELECT base_currency_id, target_currency_id, rate
                        FROM exchanges
                        WHERE target_currency_id in (
                            SELECT target_currency_id
                            FROM exchanges
                            WHERE base_currency_id = ?
                            INTERSECT
                            SELECT target_currency_id
                            FROM exchanges
                            WHERE base_currency_id = ?
                            LIMIT 1
                        )
                    ) all_cross_exchanges
                    ORDER BY base_currency_id, target_currency_id
                    LIMIT 2
                ) cross_exchanges
                INNER JOIN currencies bases ON exchanges.base_currency_id = bases.id
                INNER JOIN currencies targets ON exchanges.target_currency_id = targets.id
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
            statement.setString(1, params.baseId());
            statement.setString(2, params.targetId());
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
            statement.setString(2, params.baseId());
            statement.setString(3, params.targetId());

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
                return Optional.of(Exchange.from(
                        Currency.from(
                                UUID.fromString(cursor.getString("base_id")),
                                cursor.getString("base_code"),
                                cursor.getString("base_full_name"),
                                cursor.getString("base_sign")
                        ),

                        Currency.from(
                                UUID.fromString(cursor.getString("target_id")),
                                cursor.getString("target_code"),
                                cursor.getString("target_full_name"),
                                cursor.getString("target_sign")
                        ),

                        cursor.getBigDecimal("exchange_rate")
                ));
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
                exchanges.add(Exchange.from(
                        Currency.from(
                                UUID.fromString(cursor.getString("base_id")),
                                cursor.getString("base_code"),
                                cursor.getString("base_full_name"),
                                cursor.getString("base_sign")
                        ),

                        Currency.from(
                                UUID.fromString(cursor.getString("target_id")),
                                cursor.getString("target_code"),
                                cursor.getString("target_full_name"),
                                cursor.getString("target_sign")
                        ),

                        cursor.getBigDecimal("exchange_rate")
                ));
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
                var statement = connection.prepareStatement(Query.FetchByCode)
        ) {
            statement.setString(1, base);
            statement.setString(2, target);
            statement.setString(3, base);
            statement.setString(4, target);

            statement.execute();
            var cursor = statement.getResultSet();

            if (cursor.next()) {
                return Optional.of(new Pair<>(
                        Exchange.from(
                                Currency.from(
                                        UUID.fromString(cursor.getString("base_id")),
                                        cursor.getString("base_code"),
                                        cursor.getString("base_full_name"),
                                        cursor.getString("base_sign")
                                ),

                                Currency.from(
                                        UUID.fromString(cursor.getString("target_id")),
                                        cursor.getString("target_code"),
                                        cursor.getString("target_full_name"),
                                        cursor.getString("target_sign")
                                ),

                                cursor.getBigDecimal("exchange_rate")
                        ),

                        Exchange.from(
                                Currency.from(
                                        UUID.fromString(cursor.getString("base_id")),
                                        cursor.getString("base_code"),
                                        cursor.getString("base_full_name"),
                                        cursor.getString("base_sign")
                                ),

                                Currency.from(
                                        UUID.fromString(cursor.getString("target_id")),
                                        cursor.getString("target_code"),
                                        cursor.getString("target_full_name"),
                                        cursor.getString("target_sign")
                                ),

                                cursor.getBigDecimal("exchange_rate")
                        )
                ));
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new JdbcRepositoryException(e);
        }
    }
}
