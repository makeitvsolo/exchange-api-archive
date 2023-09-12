package com.makeitvsolo.exchangeapi.domain.exchange;

import com.makeitvsolo.exchangeapi.domain.currency.Currency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.UUID;

@DisplayName("CrossExchange")
public class CrossExchangeTests {
    private Exchange exchange;
    private Currency usd = Currency.from(UUID.randomUUID(), "USD", "United States Dollar", "$");
    private Currency cad = Currency.from(UUID.randomUUID(), "CAD", "Canadian Dollar", "C$");
    private Currency jpy = Currency.from(UUID.randomUUID(), "JPY", "Japanese Yen", "¥");
    private Currency amd = Currency.from(UUID.randomUUID(), "AMD", "Armenian Dram", "֏");

    @Test
    @DisplayName("possible if bases of both exchanges are equal")
    public void possibleIfBasesOfBothExchangesAreEqual() {
        var first = Exchange.from(usd, cad, BigDecimal.ONE);
        var second = Exchange.from(usd, jpy, BigDecimal.ONE);

        Assertions.assertDoesNotThrow(() -> exchange = Exchange.Cross.of(first, second));
    }

    @Test
    @DisplayName("possible if targets of both exchanges are equal")
    public void possibleIfTargetsOfBothExchangesAreEqual() {
        var first = Exchange.from(usd, cad, BigDecimal.ONE);
        var second = Exchange.from(usd, jpy, BigDecimal.ONE);

        Assertions.assertDoesNotThrow(() -> exchange = Exchange.Cross.of(first, second));
    }

    @Test
    @DisplayName("or throws when both exchanges are equal")
    public void orThrowsWhenBothExchangesAreEqual() {
        var first = Exchange.from(usd, cad, BigDecimal.ONE);
        var second = Exchange.from(usd, cad, BigDecimal.ONE);

        Assertions.assertThrows(WrongExchangeException.class, () -> exchange = Exchange.Cross.of(first, second));
    }

    @Test
    @DisplayName("or throws when neither bases nor targets of the exchanges are equal")
    public void orThrowsWhenNeitherBasesNorTargetsAreEqual() {
        var first = Exchange.from(usd, cad, BigDecimal.ONE);
        var second = Exchange.from(jpy, amd, BigDecimal.ONE);

        Assertions.assertThrows(WrongExchangeException.class, () -> exchange = Exchange.Cross.of(first, second));
    }

    @Test
    @DisplayName("converts amount by cross rate")
    public void convertsAmountByCrossRate() {
        var amount = BigDecimal.valueOf(25.0);
        var usdToCad = BigDecimal.valueOf(1.5);
        var usdToJpy = BigDecimal.valueOf(0.5);
        var crossRate = usdToJpy.multiply(BigDecimal.ONE.divide(usdToCad, MathContext.DECIMAL64));

        var first = Exchange.from(usd, cad, usdToCad);
        var second = Exchange.from(usd, jpy, usdToJpy);
        var leftCross = Exchange.Cross.of(first, second);
        var rightCross = Exchange.Cross.of(Exchange.Reversed.of(first), Exchange.Reversed.of(second));

        Assertions.assertEquals(amount.multiply(crossRate), leftCross.convert(amount));
        Assertions.assertEquals(amount.multiply(crossRate), rightCross.convert(amount));
    }
}
