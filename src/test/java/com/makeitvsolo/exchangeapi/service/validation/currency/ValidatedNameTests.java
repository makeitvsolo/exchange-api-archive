package com.makeitvsolo.exchangeapi.service.validation.currency;

import com.makeitvsolo.exchangeapi.service.exception.validation.InvalidPayloadException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Currency full name")
public class ValidatedNameTests {

    @Test
    @DisplayName("should be not null")
    public void shouldBeNotNull() {
        var name = new ValidatedName(null);

        Assertions.assertThrows(InvalidPayloadException.class, () -> name.validated());
    }

    @Test
    @DisplayName("should be not empty")
    public void shouldBeNotEmpty() {
        var emptyName = new ValidatedCode("");
        var blankName = new ValidatedCode("     ");

        Assertions.assertThrows(InvalidPayloadException.class, () -> emptyName.validated());
        Assertions.assertThrows(InvalidPayloadException.class, () -> blankName.validated());
    }
}
