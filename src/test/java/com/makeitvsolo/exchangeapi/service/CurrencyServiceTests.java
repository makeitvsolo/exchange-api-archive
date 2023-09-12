package com.makeitvsolo.exchangeapi.service;

import com.makeitvsolo.exchangeapi.core.unique.Unique;
import com.makeitvsolo.exchangeapi.datasource.CurrencyRepository;
import com.makeitvsolo.exchangeapi.domain.Currency;
import com.makeitvsolo.exchangeapi.domain.mapping.MappedFromCurrency;
import com.makeitvsolo.exchangeapi.service.dto.currency.CreateCurrencyDto;
import com.makeitvsolo.exchangeapi.service.dto.currency.CurrencyDto;
import com.makeitvsolo.exchangeapi.service.dto.currency.CurrencyListDto;
import com.makeitvsolo.exchangeapi.service.exception.currency.CurrencyNotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DisplayName("CurrencyService")
public class CurrencyServiceTests {
    private CurrencyService service;
    @Mock
    private CurrencyRepository repository;
    @Mock
    private Unique<UUID> currencyId;
    @Mock
    private MappedFromCurrency<CurrencyDto> mapper;

    private AutoCloseable closeable;

    @BeforeEach
    public void beforeEach() {
        closeable = MockitoAnnotations.openMocks(this);
        Mockito.when(currencyId.unique())
                .thenReturn(UUID.randomUUID());

        service = new CurrencyService(repository, currencyId, mapper);
    }

    @AfterEach
    public void afterEach() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("saves currency after creation")
    public void savesCurrencyAfterCreation() {
        var payload = new CreateCurrencyDto("USD", "United States Dollar", "$");

        service.create(payload);

        Mockito.verify(repository)
                .save(Currency.create(currencyId, payload.code(), payload.fullName(), payload.sign()));
    }

    @Test
    @DisplayName("retrieves currency by code when it's contained in repository")
    public void retrievesCurrencyByCodeWheItIsContainedInRepository() {
        var usdCode = "USD";
        var usdDto = new CurrencyDto(UUID.randomUUID(), usdCode, "United States Dollar", "$");
        var usd = Currency.from(usdDto.id(), usdDto.code(), usdDto.fullName(), usdDto.sign());

        Mockito.when(repository.fetchByCode(usdCode))
                       .thenReturn(Optional.of(usd));
        Mockito.when(mapper.from(usdDto.id(), usdDto.code(), usdDto.fullName(), usdDto.sign()))
                       .thenReturn(usdDto);

        Assertions.assertEquals(usdDto, service.byCode(usdCode));
    }

    @Test
    @DisplayName("or throws when it isn't contained in repository")
    public void orThrowsWhenItIsNotContainedInRepository() {
        var usdCode = "USD";

        Mockito.when(repository.fetchByCode(usdCode))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(CurrencyNotFoundException.class, () -> service.byCode(usdCode));
    }

    @Test
    @DisplayName("retrieves all currencies")
    public void retrievesAllCurrencies() {
        var usdDto = new CurrencyDto(UUID.randomUUID(), "USD", "United States Dollar", "$");
        var cadDto = new CurrencyDto(UUID.randomUUID(), "CAD", "Canadian Dollar", "C$");
        var usd = Currency.from(usdDto.id(), usdDto.code(), usdDto.fullName(), usdDto.sign());
        var cad = Currency.from(cadDto.id(), cadDto.code(), cadDto.fullName(), cadDto.sign());

        var currencies = new CurrencyListDto(List.of(usdDto, cadDto));

        Mockito.when(repository.fetchAll())
                .thenReturn(List.of(usd, cad));
        Mockito.when(mapper.from(usdDto.id(), usdDto.code(), usdDto.fullName(), usdDto.sign()))
                .thenReturn(usdDto);
        Mockito.when(mapper.from(cadDto.id(), cadDto.code(), cadDto.fullName(), cadDto.sign()))
                .thenReturn(cadDto);

        Assertions.assertEquals(currencies, service.all());
    }
}
