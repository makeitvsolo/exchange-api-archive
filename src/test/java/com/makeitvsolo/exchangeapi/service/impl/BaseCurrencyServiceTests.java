package com.makeitvsolo.exchangeapi.service.impl;

import com.makeitvsolo.exchangeapi.datasource.CurrencyRepository;
import com.makeitvsolo.exchangeapi.domain.Currency;
import com.makeitvsolo.exchangeapi.domain.mapping.MappedFromCurrency;
import com.makeitvsolo.exchangeapi.service.CurrencyService;
import com.makeitvsolo.exchangeapi.service.dto.currency.CreateCurrencyDto;
import com.makeitvsolo.exchangeapi.service.dto.currency.CurrencyDto;
import com.makeitvsolo.exchangeapi.service.dto.currency.CurrencyListDto;
import com.makeitvsolo.exchangeapi.service.exception.currency.CurrencyAlreadyExistsException;
import com.makeitvsolo.exchangeapi.service.exception.currency.CurrencyNotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

@DisplayName("BaseCurrencyService")
public class BaseCurrencyServiceTests {
    private CurrencyService service;
    @Mock
    private CurrencyRepository repository;
    @Mock
    private MappedFromCurrency<CurrencyDto> mapper;

    private AutoCloseable closeable;

    @BeforeEach
    public void beforeEach() {
        closeable = MockitoAnnotations.openMocks(this);

        service = new BaseCurrencyService(repository, mapper);
    }

    @AfterEach
    public void afterEach() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("saves currency after creation")
    public void savesCurrencyAfterCreation() {
        var usdDto = new CurrencyDto("USD", "United States Dollar", "$");

        var payload = new CreateCurrencyDto("USD", "United States Dollar", "$");
        Mockito.when(repository.fetchByCode("USD"))
                       .thenReturn(Optional.empty());
        Mockito.when(mapper.from(usdDto.code(), usdDto.fullName(), usdDto.sign()))
                       .thenReturn(usdDto);

        Assertions.assertEquals(usdDto, service.create(payload));
        Mockito.verify(repository)
                .save(Currency.create(payload.code(), payload.fullName(), payload.sign()));
    }

    @Test
    @DisplayName("or throws when currency already exists")
    public void orThrowsWhenCurrencyAlreadyExists() {
        var payload = new CreateCurrencyDto("USD", "United States Dollar", "$");
        Mockito.when(repository.fetchByCode("USD"))
                .thenReturn(
                        Optional.of(
                                Currency.from("USD", "United States Dollar", "$")
                        )
                );

        Assertions.assertThrows(CurrencyAlreadyExistsException.class, () -> service.create(payload));
    }

    @Test
    @DisplayName("retrieves currency by code when it's contained in repository")
    public void retrievesCurrencyByCodeWheItIsContainedInRepository() {
        var usdCode = "USD";
        var usdDto = new CurrencyDto(usdCode, "United States Dollar", "$");
        var usd = Currency.from(usdDto.code(), usdDto.fullName(), usdDto.sign());

        Mockito.when(repository.fetchByCode(usdCode))
                       .thenReturn(Optional.of(usd));
        Mockito.when(mapper.from(usdDto.code(), usdDto.fullName(), usdDto.sign()))
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
        var usdDto = new CurrencyDto("USD", "United States Dollar", "$");
        var cadDto = new CurrencyDto("CAD", "Canadian Dollar", "C$");
        var usd = Currency.from(usdDto.code(), usdDto.fullName(), usdDto.sign());
        var cad = Currency.from(cadDto.code(), cadDto.fullName(), cadDto.sign());

        var currencies = new CurrencyListDto(List.of(usdDto, cadDto));

        Mockito.when(repository.fetchAll())
                .thenReturn(List.of(usd, cad));
        Mockito.when(mapper.from(usdDto.code(), usdDto.fullName(), usdDto.sign()))
                .thenReturn(usdDto);
        Mockito.when(mapper.from(cadDto.code(), cadDto.fullName(), cadDto.sign()))
                .thenReturn(cadDto);

        Assertions.assertEquals(currencies, service.all());
    }
}
