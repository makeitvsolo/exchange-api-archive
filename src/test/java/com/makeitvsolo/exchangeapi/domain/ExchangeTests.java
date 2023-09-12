package com.makeitvsolo.exchangeapi.domain;

import com.makeitvsolo.exchangeapi.domain.exception.WrongExchangeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

@DisplayName("Exchange")
public class ExchangeTests {
    private Exchange exchange;
    private Currency usd = Currency.from(UUID.randomUUID(), "USD", "United States Dollar", "$");
    private Currency cad = Currency.from(UUID.randomUUID(), "CAD", "Canadian Dollar", "C$");

    @Test
    @DisplayName("can be between different currencies")
    public void canBeBetweenDifferentCurrencies() {

        Assertions.assertDoesNotThrow(() -> Exchange.from(usd, cad, BigDecimal.ONE));
    }

    @Test
    @DisplayName("or throws when currencies are equals")
    public void orThrowsWhenCurrenciesAreEquals() {

        Assertions.assertThrows(WrongExchangeException.class, () -> Exchange.from(usd, usd, BigDecimal.ONE));
    }

    @Test
    @DisplayName("rate should be `>0`")
    public void rateShouldBeGreaterThanZero() {
        var rate = BigDecimal.ONE;

        Assertions.assertDoesNotThrow(() -> exchange = Exchange.from(usd, cad, rate));
    }

    @Test
    @DisplayName("or throws when rate `<=0`")
    public void orThrowsWhenRateLesserEqualsThanZero() {
        var wrongRate = BigDecimal.ZERO;

        Assertions.assertThrows(WrongExchangeException.class, () -> exchange = Exchange.from(usd, cad, wrongRate));
    }

    @Test
    @DisplayName("can be updated")
    public void canBeUpdated() {
        var rate = BigDecimal.valueOf(1.5);
        exchange = Exchange.from(usd, cad, BigDecimal.ONE);

        Assertions.assertEquals(rate, exchange.updated(rate).rate());
    }

    @Test
    @DisplayName("or throws when new rate `<=0`")
    public void orThrowsWhenNewRateLesserEqualsThanZero() {
        var wrongRate = BigDecimal.ZERO;
        exchange = Exchange.from(usd, cad, BigDecimal.ONE);

        Assertions.assertThrows(WrongExchangeException.class, () -> exchange.updated(wrongRate));
    }

    @Test
    @DisplayName("converts amount by rate")
    public void convertsAmountByRate() {
        var rate = BigDecimal.TEN;
        var amount = BigDecimal.valueOf(1.5);

        exchange = Exchange.from(usd, cad, BigDecimal.TEN);

        Assertions.assertEquals(amount.multiply(rate), exchange.convert(amount));
    }
}
