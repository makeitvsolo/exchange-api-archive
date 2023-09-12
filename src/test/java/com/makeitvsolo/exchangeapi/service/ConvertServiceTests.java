package com.makeitvsolo.exchangeapi.service;

import com.makeitvsolo.exchangeapi.datasource.ExchangeRepository;
import com.makeitvsolo.exchangeapi.domain.Currency;
import com.makeitvsolo.exchangeapi.domain.Exchange;
import com.makeitvsolo.exchangeapi.domain.mapping.MappedFromCurrency;
import com.makeitvsolo.exchangeapi.service.dto.convert.ConvertAmountDto;
import com.makeitvsolo.exchangeapi.service.dto.convert.ConvertedAmountDto;
import com.makeitvsolo.exchangeapi.service.dto.currency.CurrencyDto;
import com.makeitvsolo.exchangeapi.service.exception.exchange.ExchangeNotFoundException;
import org.javatuples.Pair;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@DisplayName("ConvertService")
public class ConvertServiceTests {
    private ConvertService service;
    @Mock
    private ExchangeRepository repository;
    @Mock
    private MappedFromCurrency<CurrencyDto> mapper;
    private ConvertAmountDto payload = new ConvertAmountDto("USD", "CAD", BigDecimal.ONE);
    private CurrencyDto usdDto = new CurrencyDto(new UUID(0, 0), "USD", "United States Dollar", "$");
    private CurrencyDto cadDto = new CurrencyDto(new UUID(0, 1), "CAD", "Canadian Dollar", "C$");
    private ConvertedAmountDto convertedDto = new ConvertedAmountDto(
            usdDto,
            cadDto,
            BigDecimal.ONE,
            BigDecimal.ONE,
            BigDecimal.ONE
    );
    private Currency usd = Currency.from(new UUID(0, 0), "USD", "United States Dollar", "$");
    private Currency cad = Currency.from(new UUID(0, 1), "CAD", "Canadian Dollar", "C$");
    private Currency jpy = Currency.from(new UUID(0, 2), "JPY", "Japanese Yen", "¥");

    private AutoCloseable closeable;

    @BeforeEach
    public void beforeEach() {
        closeable = MockitoAnnotations.openMocks(this);

        Mockito.when(mapper.from(usdDto.id(), usdDto.code(), usdDto.fullName(), usdDto.sign()))
                .thenReturn(usdDto);
        Mockito.when(mapper.from(cadDto.id(), cadDto.code(), cadDto.fullName(), cadDto.sign()))
                .thenReturn(cadDto);

        service = new ConvertService(repository, mapper);
    }

    @AfterEach
    public void afterEach() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("converts amount directly")
    public void convertsAmountDirectly() {
        Mockito.when(repository.fetchByCode("USD", "CAD"))
                .thenReturn(Optional.of(Exchange.from(usd, cad, BigDecimal.ONE)));

        Assertions.assertEquals(convertedDto, service.convert(payload));
    }

    @Test
    @DisplayName("converts amount reversely when direct method is unavailable")
    public void convertsAmountReverselyWhenDirectMethodIsUnavailable() {
        Mockito.when(repository.fetchByCode("USD", "CAD"))
                .thenReturn(Optional.empty());
        Mockito.when(repository.fetchByCode("CAD", "USD"))
                .thenReturn(Optional.of(Exchange.from(cad, usd, BigDecimal.ONE)));

        Assertions.assertEquals(convertedDto, service.convert(payload));
    }

    @Test
    @DisplayName("converts amount by crossing when both other methods are unavailable")
    public void convertsAmountByCrossingWhenBothOtherAreUnavailable() {
        Mockito.when(repository.fetchByCode("USD", "CAD"))
                .thenReturn(Optional.empty());
        Mockito.when(repository.fetchByCode("CAD", "USD"))
                .thenReturn(Optional.empty());
        Mockito.when(repository.fetchAnyCrossPair("USD", "CAD"))
                       .thenReturn(
                               Optional.of(
                                       Pair.with(
                                               Exchange.from(usd, jpy, BigDecimal.ONE),
                                               Exchange.from(cad, jpy, BigDecimal.ONE)
                                       )
                               )
                       );

        Assertions.assertEquals(convertedDto, service.convert(payload));
    }

    @Test
    @DisplayName("or throws when all methods are unavailable")
    public void orThrowsWhenAllMethodsAreUnavailable() {
        Mockito.when(repository.fetchByCode("USD", "CAD"))
                .thenReturn(Optional.empty());
        Mockito.when(repository.fetchByCode("CAD", "USD"))
                .thenReturn(Optional.empty());
        Mockito.when(repository.fetchAnyCrossPair("USD", "CAD"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ExchangeNotFoundException.class, () -> service.convert(payload));
    }
}
