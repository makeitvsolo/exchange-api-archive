package com.makeitvsolo.exchangeapi.service.validation.currency;

import com.makeitvsolo.exchangeapi.service.exception.validation.InvalidPayloadException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Currency sign")
public class ValidatedSignTests {

    @Test
    @DisplayName("should be not null")
    public void shouldBeNotNull() {
        var sign = new ValidatedSign(null);

        Assertions.assertThrows(InvalidPayloadException.class, () -> sign.validated());
    }

    @Test
    @DisplayName("should be not empty")
    public void shouldBeNotEmpty() {
        var emptySign = new ValidatedCode("");
        var blankSign = new ValidatedCode("     ");

        Assertions.assertThrows(InvalidPayloadException.class, () -> emptySign.validated());
        Assertions.assertThrows(InvalidPayloadException.class, () -> blankSign.validated());
    }
}
