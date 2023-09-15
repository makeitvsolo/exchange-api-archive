package com.makeitvsolo.exchangeapi.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeitvsolo.exchangeapi.ApplicationConfig;
import com.makeitvsolo.exchangeapi.domain.exception.InvalidCurrencyCodeException;
import com.makeitvsolo.exchangeapi.service.ConvertService;
import com.makeitvsolo.exchangeapi.service.dto.convert.ConvertAmountDto;
import com.makeitvsolo.exchangeapi.service.exception.exchange.ExchangeNotFoundException;
import com.makeitvsolo.exchangeapi.service.exception.validation.InvalidPayloadException;
import com.makeitvsolo.exchangeapi.servlet.exception.ParseQueryException;
import com.makeitvsolo.exchangeapi.servlet.message.ErrorMessage;
import com.makeitvsolo.exchangeapi.servlet.query.ParseQuery;
import com.makeitvsolo.exchangeapi.servlet.query.convert.ParseAmount;
import com.makeitvsolo.exchangeapi.servlet.query.convert.ParseConvertQuery;
import com.makeitvsolo.exchangeapi.servlet.query.currency.ParseCurrencyCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "convert", urlPatterns = "/convert")
public final class ConvertServlet extends HttpServlet {
    private final ConvertService service = ApplicationConfig.Services.Convert.configured();
    private final ParseQuery<ConvertAmountDto> query = new ParseConvertQuery(new ParseCurrencyCode(), new ParseAmount());
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.setStatus(HttpServletResponse.SC_OK);

            var amount = query.parse(req.getQueryString());
            var converted = service.convert(amount);

            objectMapper.writeValue(resp.getWriter(), converted);
        } catch (ParseQueryException | InvalidPayloadException | InvalidCurrencyCodeException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            var message = new ErrorMessage(e.getMessage());
            objectMapper.writeValue(resp.getWriter(), message);
        } catch (ExchangeNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

            var message = new ErrorMessage(e.getMessage());
            objectMapper.writeValue(resp.getWriter(), message);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            var message = new ErrorMessage(e.getMessage());
            objectMapper.writeValue(resp.getWriter(), message);
        }
    }
}
