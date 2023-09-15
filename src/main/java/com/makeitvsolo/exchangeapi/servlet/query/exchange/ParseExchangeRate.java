package com.makeitvsolo.exchangeapi.servlet.query.exchange;

import com.makeitvsolo.exchangeapi.servlet.exception.ParsePayloadException;
import com.makeitvsolo.exchangeapi.servlet.query.ParsePayload;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;

public final class ParseExchangeRate implements ParsePayload<BigDecimal> {

    @Override
    public BigDecimal parseFrom(BufferedReader reader) {
        try (reader) {
            var data = reader.readLine();

            if (data == null) {
                throw new ParsePayloadException("Payload is null");
            }

            var dataParams = data.split("&");

            if (dataParams.length != 1) {
                throw new ParsePayloadException("Payload should contains `rate=`");
            }

            var rateString = dataParams[0].replaceAll("rate=", "");
            if (rateString.length() == dataParams[0].length()) {
                throw new ParsePayloadException("Payload should contains `rate=`");
            }

            return BigDecimal.valueOf(Double.parseDouble(rateString));
        } catch (IOException e) {
            throw new ParsePayloadException(e.getMessage());
        } catch (NumberFormatException e) {
            throw new ParsePayloadException("Given `rate=` is not a number");
        }
    }
}
