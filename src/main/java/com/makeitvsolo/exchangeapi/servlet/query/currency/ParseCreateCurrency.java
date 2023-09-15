package com.makeitvsolo.exchangeapi.servlet.query.currency;

import com.makeitvsolo.exchangeapi.service.dto.currency.CreateCurrencyDto;
import com.makeitvsolo.exchangeapi.servlet.exception.ParsePayloadException;
import com.makeitvsolo.exchangeapi.servlet.query.ParsePayload;

import java.io.BufferedReader;
import java.io.IOException;

public final class ParseCreateCurrency implements ParsePayload<CreateCurrencyDto> {

    public ParseCreateCurrency() {
    }

    @Override
    public CreateCurrencyDto parseFrom(BufferedReader reader) {
        try (reader) {
            var data = reader.readLine();

            if (data == null) {
                throw new ParsePayloadException("Payload is null");
            }

            var dataParams = data.split("&");

            if (dataParams.length != 3) {
                throw new ParsePayloadException("Payload should contains `code=`, `fullName=`, `sign=`");
            }

            var code = dataParams[0].replaceAll("code=", "");
            if (code.length() == dataParams[0].length()) {
                throw new ParsePayloadException("Payload first arg is `code=`");
            }

            var fullName = dataParams[1].replaceAll("fullName=", "");
            if (fullName.length() == dataParams[1].length()) {
                throw new ParsePayloadException("Payload second arg is `fullName=`");
            }

            var sign = dataParams[2].replaceAll("sign=", "");
            if (sign.length() == dataParams[2].length()) {
                throw new ParsePayloadException("Payload third arg is `sign=`");
            }

            return new CreateCurrencyDto(code, fullName, sign);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
