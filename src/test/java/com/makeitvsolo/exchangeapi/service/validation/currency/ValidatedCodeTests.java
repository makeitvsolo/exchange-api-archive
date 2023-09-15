package com.makeitvsolo.exchangeapi.service.validation.currency;

import com.makeitvsolo.exchangeapi.service.exception.validation.InvalidPayloadException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Currency code")
public class ValidatedCodeTests {

    @Test
    @DisplayName("should be not null")
    public void shouldBeNotNull() {
        var code = new ValidatedCode(null);

        Assertions.assertThrows(InvalidPayloadException.class, () -> code.validated());
    }

    @Test
    @DisplayName("should be not empty")
    public void shouldBeNotEmpty() {
        var emptyCode = new ValidatedCode("");
        var blankCode = new ValidatedCode("     ");

        Assertions.assertThrows(InvalidPayloadException.class, () -> emptyCode.validated());
        Assertions.assertThrows(InvalidPayloadException.class, () -> blankCode.validated());
    }
}
