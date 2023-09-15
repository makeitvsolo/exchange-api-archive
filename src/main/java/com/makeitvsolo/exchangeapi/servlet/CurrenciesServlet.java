package com.makeitvsolo.exchangeapi.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeitvsolo.exchangeapi.ApplicationConfig;
import com.makeitvsolo.exchangeapi.domain.exception.InvalidCurrencyCodeException;
import com.makeitvsolo.exchangeapi.service.CurrencyService;
import com.makeitvsolo.exchangeapi.service.dto.currency.CreateCurrencyDto;
import com.makeitvsolo.exchangeapi.service.exception.currency.CurrencyAlreadyExistsException;
import com.makeitvsolo.exchangeapi.service.exception.validation.InvalidPayloadException;
import com.makeitvsolo.exchangeapi.servlet.exception.ParsePayloadException;
import com.makeitvsolo.exchangeapi.servlet.message.ErrorMessage;
import com.makeitvsolo.exchangeapi.servlet.query.ParsePayload;
import com.makeitvsolo.exchangeapi.servlet.query.currency.ParseCreateCurrency;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "currencies", urlPatterns = "/currencies")
public final class CurrenciesServlet extends HttpServlet {
    private final CurrencyService service = ApplicationConfig.Services.Currency.configured();
    private final ParsePayload<CreateCurrencyDto> payload = new ParseCreateCurrency();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.setStatus(HttpServletResponse.SC_OK);

            var currencies = service.all();
            objectMapper.writeValue(resp.getWriter(), currencies);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            var message = new ErrorMessage(e.getMessage());
            objectMapper.writeValue(resp.getWriter(), message);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.setStatus(HttpServletResponse.SC_OK);

            var currency = service.create(payload.parseFrom(req.getReader()));
            objectMapper.writeValue(resp.getWriter(), currency);
        } catch (ParsePayloadException | InvalidPayloadException | InvalidCurrencyCodeException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            var message = new ErrorMessage(e.getMessage());
            objectMapper.writeValue(resp.getWriter(), message);
        } catch (CurrencyAlreadyExistsException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);

            var message = new ErrorMessage(e.getMessage());
            objectMapper.writeValue(resp.getWriter(), message);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            var message = new ErrorMessage("Internal server error");
            objectMapper.writeValue(resp.getWriter(), message);
        }
    }
}
