package com.makeitvsolo.exchangeapi.service.exchange;

import com.makeitvsolo.exchangeapi.datasource.CurrencyRepository;
import com.makeitvsolo.exchangeapi.datasource.ExchangeRepository;
import com.makeitvsolo.exchangeapi.domain.Currency;
import com.makeitvsolo.exchangeapi.domain.Exchange;
import com.makeitvsolo.exchangeapi.domain.mapping.MappedFromExchange;
import com.makeitvsolo.exchangeapi.service.currency.dto.CurrencyDto;
import com.makeitvsolo.exchangeapi.service.currency.exception.CurrencyNotFoundException;
import com.makeitvsolo.exchangeapi.service.exchange.dto.*;
import com.makeitvsolo.exchangeapi.service.exchange.exception.ExchangeNotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DisplayName("ExchangeService")
public class ExchangeServiceTests {
    private ExchangeService service;
    private Currency usd = Currency.from(UUID.randomUUID(), "USD", "United States Dollar", "$");
    private Currency cad = Currency.from(UUID.randomUUID(), "CAD", "Canadian Dollar", "C$");
    @Mock
    private ExchangeRepository exchangeRepository;
    @Mock
    private CurrencyRepository currencyRepository;
    @Mock
    private MappedFromExchange<ExchangeDto> mapper;

    private AutoCloseable closeable;

    @BeforeEach
    public void beforeEach() {
        closeable = MockitoAnnotations.openMocks(this);

        service = new ExchangeService(exchangeRepository, currencyRepository, mapper);
    }

    @AfterEach
    public void afterEach() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("saves exchange after creation")
    public void savesExchangeAfterCreation() {
        var payload = new CreateExchangeDto("USD", "CAD", BigDecimal.ONE);

        Mockito.when(currencyRepository.fetchByCode("USD"))
                .thenReturn(Optional.of(usd));
        Mockito.when(currencyRepository.fetchByCode("CAD"))
                .thenReturn(Optional.of(cad));

        service.create(payload);

        Mockito.verify(exchangeRepository).save(
                Exchange.create(usd, cad, BigDecimal.ONE)
        );
    }

    @Test
    @DisplayName("or throws when currencies doesn't contains in repository")
    public void orThrowsWhenCurrenciesDoesNotContainsInRepository() {
        var payload = new CreateExchangeDto("USD", "CAD", BigDecimal.ONE);

        Mockito.when(currencyRepository.fetchByCode("USD"))
                .thenReturn(Optional.empty());
        Mockito.when(currencyRepository.fetchByCode("CAD"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(CurrencyNotFoundException.class, () -> service.create(payload));
    }

    @Test
    @DisplayName("updates exchange after rate changes")
    public void updatesExchangeAfterRateChanges() {
        var payload = new UpdateExchangeDto("USD", "CAD", BigDecimal.TEN);
        var exchange = Exchange.from(usd, cad, BigDecimal.ONE);

        Mockito.when(exchangeRepository.fetchByCode("USD", "CAD"))
                .thenReturn(Optional.of(exchange));

        service.update(payload);

        Mockito.verify(exchangeRepository).update(exchange.updated(BigDecimal.TEN));
    }

    @Test
    @DisplayName("or throws when exchange doesn't contains in repository")
    public void orThrowsWhenExchangeDoesNotContainsInRepository() {
        var payload = new UpdateExchangeDto("USD", "CAD", BigDecimal.TEN);

        Mockito.when(exchangeRepository.fetchByCode("USD", "CAD"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ExchangeNotFoundException.class, () -> service.update(payload));
    }

    @Test
    @DisplayName("retrieves exchange by code when it's contained in repository")
    public void retrievesExchangeByCodeWhenItIsContainedInRepository() {
        var code = new ExchangeCodeDto("USD", "CAD");
        var exchange = Exchange.from(usd, cad, BigDecimal.ONE);
        var exchangeDto = new ExchangeDto(
                new CurrencyDto(UUID.randomUUID(), "USD", "United States Dollar", "$"),
                new CurrencyDto(UUID.randomUUID(), "CAD", "Canadian Dollar", "C$"),
                BigDecimal.ONE
        );

        Mockito.when(exchangeRepository.fetchByCode(code.base(), code.target()))
                .thenReturn(Optional.of(exchange));
        Mockito.when(mapper.from(usd, cad, BigDecimal.ONE))
                .thenReturn(exchangeDto);

        Assertions.assertEquals(exchangeDto, service.byCode(code));
    }

    @Test
    @DisplayName("or throws when it isn't contained in repository")
    public void orThrowsWhenItIsNotContainedInRepository() {
        var code = new ExchangeCodeDto("USD", "CAD");

        Mockito.when(exchangeRepository.fetchByCode(code.base(), code.target()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ExchangeNotFoundException.class, () -> service.byCode(code));
    }

    @Test
    @DisplayName("retrieves all exchanges")
    public void retrievesAllExchanges() {
        var usdToCad = Exchange.from(usd, cad, BigDecimal.ONE);
        var usdToCadDto = new ExchangeDto(
                new CurrencyDto(UUID.randomUUID(), "USD", "United States Dollar", "$"),
                new CurrencyDto(UUID.randomUUID(), "CAD", "Canadian Dollar", "C$"),
                BigDecimal.ONE
        );

        var exchanges = new ExchangeListDto(List.of(usdToCadDto));

        Mockito.when(exchangeRepository.fetchAll())
                .thenReturn(List.of(usdToCad));
        Mockito.when(mapper.from(usd, cad, BigDecimal.ONE))
                .thenReturn(usdToCadDto);

        Assertions.assertEquals(exchanges, service.all());
    }
}
