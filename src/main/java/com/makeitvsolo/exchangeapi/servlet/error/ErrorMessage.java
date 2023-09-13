package com.makeitvsolo.exchangeapi.servlet.error;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonDeserialize
public record ErrorMessage(String message) {
}
