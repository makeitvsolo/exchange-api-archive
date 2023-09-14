package com.makeitvsolo.exchangeapi;

import com.makeitvsolo.exchangeapi.core.unique.Unique;
import com.makeitvsolo.exchangeapi.core.unique.UniqueUUID;
import com.makeitvsolo.exchangeapi.datasource.CurrencyRepository;
import com.makeitvsolo.exchangeapi.datasource.ExchangeRepository;
import com.makeitvsolo.exchangeapi.datasource.jdbc.JdbcCurrencyRepository;
import com.makeitvsolo.exchangeapi.datasource.jdbc.JdbcExchangeRepository;
import com.makeitvsolo.exchangeapi.datasource.jdbc.mapping.MappedFromCurrencyToId;
import com.makeitvsolo.exchangeapi.datasource.jdbc.mapping.MappedFromCurrencyToInsert;
import com.makeitvsolo.exchangeapi.datasource.jdbc.mapping.MappedFromExchangeToInsert;
import com.makeitvsolo.exchangeapi.datasource.jdbc.mapping.MappedFromExchangeToUpdate;
import com.makeitvsolo.exchangeapi.datasource.jdbc.parameter.currency.InsertCurrencyParameters;
import com.makeitvsolo.exchangeapi.datasource.jdbc.parameter.exchange.InsertExchangeParameters;
import com.makeitvsolo.exchangeapi.datasource.jdbc.parameter.exchange.UpdateExchangeParameters;
import com.makeitvsolo.exchangeapi.domain.mapping.MappedFromCurrency;
import com.makeitvsolo.exchangeapi.domain.mapping.MappedFromExchange;
import com.makeitvsolo.exchangeapi.service.ConvertService;
import com.makeitvsolo.exchangeapi.service.CurrencyService;
import com.makeitvsolo.exchangeapi.service.ExchangeService;
import com.makeitvsolo.exchangeapi.service.dto.currency.CurrencyDto;
import com.makeitvsolo.exchangeapi.service.dto.exchange.ExchangeDto;
import com.makeitvsolo.exchangeapi.service.impl.BaseConvertService;
import com.makeitvsolo.exchangeapi.service.impl.BaseCurrencyService;
import com.makeitvsolo.exchangeapi.service.impl.BaseExchangeService;
import com.makeitvsolo.exchangeapi.service.mapping.MappedFromCurrencyToDto;
import com.makeitvsolo.exchangeapi.service.mapping.MappedFromExchangeToDto;
import com.makeitvsolo.exchangeapi.service.validation.WithValidationConvertService;
import com.makeitvsolo.exchangeapi.service.validation.WithValidationCurrencyService;
import com.makeitvsolo.exchangeapi.service.validation.WithValidationExchangeService;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.util.UUID;

public interface ApplicationConfig {

    interface Connections {

        final class DBCP {
            private static final BasicDataSource source = new BasicDataSource();

            public DBCP() {
            } static {
                source.setUrl(System.getenv("DB_URL"));
                source.setUsername(System.getenv("DB_USERNAME"));
                source.setPassword(System.getenv("DB_PASSWORD"));
            }

            public static DataSource source() {
                return source;
            }
        }
    }

    interface Services {

        final class Currency {

            public static CurrencyService configured() {
                return new WithValidationCurrencyService(
                        new BaseCurrencyService(
                                Repositories.Currency.configured(),
                                Uniques.UUID.configured(),
                                Mappers.FromCurrency.ToDto.configured()
                        )
                );
            }
        }

        final class Exchange {

            public static ExchangeService configured() {
                return new WithValidationExchangeService(
                        new BaseExchangeService(
                                Repositories.Exchange.configured(),
                                Repositories.Currency.configured(),
                                Mappers.FromExchange.ToDto.configured()
                        )
                );
            }
        }

        final class Convert {

            public static ConvertService configured() {
                return new WithValidationConvertService(
                        new BaseConvertService(
                                Repositories.Exchange.configured(),
                                Mappers.FromCurrency.ToDto.configured()
                        )
                );
            }
        }
    }

    interface Repositories {

        final class Currency {

            public static CurrencyRepository configured() {
                return new JdbcCurrencyRepository(
                        Connections.DBCP.source(),
                        Mappers.FromCurrency.ToInsertParameters.configured()
                );
            }
        }

        final class Exchange {

            public static ExchangeRepository configured() {
                return new JdbcExchangeRepository(
                        Connections.DBCP.source(),
                        Mappers.FromExchange.ToInsertParameters.configured(),
                        Mappers.FromExchange.ToUpdateParameters.configured()
                );
            }
        }
    }

    interface Uniques {

        final class UUID {

            public static Unique<java.util.UUID> configured() {
                return new UniqueUUID();
            }
        }
    }

    interface Mappers {

        interface FromCurrency {

            final class ToDto {

                public static MappedFromCurrency<CurrencyDto> configured() {
                    return new MappedFromCurrencyToDto();
                }
            }

            final class ToId {

                public static MappedFromCurrency<UUID> configured() {
                    return new MappedFromCurrencyToId();
                }
            }

            final class ToInsertParameters {

                public static MappedFromCurrency<InsertCurrencyParameters> configured() {
                    return new MappedFromCurrencyToInsert();
                }
            }
        }

        interface FromExchange {

            final class ToDto {

                public static MappedFromExchange<ExchangeDto> configured() {
                    return new MappedFromExchangeToDto(ApplicationConfig.Mappers.FromCurrency.ToDto.configured());
                }
            }

            final class ToInsertParameters {

                public static MappedFromExchange<InsertExchangeParameters> configured() {
                    return new MappedFromExchangeToInsert(ApplicationConfig.Mappers.FromCurrency.ToId.configured());
                }
            }

            final class ToUpdateParameters {

                public static MappedFromExchange<UpdateExchangeParameters> configured() {
                    return new MappedFromExchangeToUpdate(ApplicationConfig.Mappers.FromCurrency.ToId.configured());
                }
            }
        }
    }
}
