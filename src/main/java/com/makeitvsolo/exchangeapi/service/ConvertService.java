package com.makeitvsolo.exchangeapi.service;

import com.makeitvsolo.exchangeapi.service.dto.convert.ConvertAmountDto;
import com.makeitvsolo.exchangeapi.service.dto.convert.ConvertedAmountDto;

public interface ConvertService {

    ConvertedAmountDto convert(ConvertAmountDto payload);
}
