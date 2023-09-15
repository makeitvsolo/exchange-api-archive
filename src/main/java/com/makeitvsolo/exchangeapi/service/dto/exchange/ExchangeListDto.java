package com.makeitvsolo.exchangeapi.service.dto.exchange;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize
@JsonDeserialize
public record ExchangeListDto(List<ExchangeDto> exchanges) {
}
