package com.makeitvsolo.exchangeapi.domain;

import com.makeitvsolo.exchangeapi.domain.exception.InvalidCurrencyCodeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

@DisplayName("Currency")
public class CurrencyTests {

    @Test
    @DisplayName("code should be in ISO 4217 format")
    public void codeShouldBeInISOFormat() {
        var wrongCode = "wrong code";

        Assertions.assertThrows(
                InvalidCurrencyCodeException.class,
                () -> Currency.from(wrongCode, "Wrong Currency", "Wrong Sing")
        );
    }
}
