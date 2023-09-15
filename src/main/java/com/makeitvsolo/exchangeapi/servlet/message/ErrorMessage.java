package com.makeitvsolo.exchangeapi.servlet.message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonDeserialize
public record ErrorMessage(String message) {
}
