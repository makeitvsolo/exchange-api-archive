package com.makeitvsolo.exchangeapi.servlet.query.exchange;

import com.makeitvsolo.exchangeapi.service.dto.exchange.CreateExchangeDto;
import com.makeitvsolo.exchangeapi.servlet.exception.ParsePayloadException;
import com.makeitvsolo.exchangeapi.servlet.query.ParsePayload;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;

public final class ParseCreateExchange implements ParsePayload<CreateExchangeDto> {

    public ParseCreateExchange() {
    }

    @Override
    public CreateExchangeDto parseFrom(BufferedReader reader) {
        try (reader) {
            var data = reader.readLine();

            if (data == null) {
                throw new ParsePayloadException("Payload is null");
            }

            var dataParams = data.split("&");

            if (dataParams.length != 3) {
                throw new ParsePayloadException("Payload should contains `base=`, `target=`, `rate=`");
            }

            var base = dataParams[0].replaceAll("base=", "");
            if (base.length() == dataParams[0].length()) {
                throw new ParsePayloadException("Payload first arg is `base=`");
            }

            var target = dataParams[1].replaceAll("target=", "");
            if (target.length() == dataParams[1].length()) {
                throw new ParsePayloadException("Payload second arg is `target=`");
            }

            var rateString = dataParams[2].replaceAll("rate=", "");
            if (rateString.length() == dataParams[2].length()) {
                throw new ParsePayloadException("Payload third arg is `rate=`");
            }

            return new CreateExchangeDto(base, target, BigDecimal.valueOf(Double.parseDouble(rateString)));
        } catch (NumberFormatException e) {
            throw new ParsePayloadException("Given `rate=` is not a number");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
