package com.makeitvsolo.exchangeapi.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeitvsolo.exchangeapi.ApplicationConfig;
import com.makeitvsolo.exchangeapi.domain.exception.InvalidCurrencyCodeException;
import com.makeitvsolo.exchangeapi.domain.exception.WrongExchangeException;
import com.makeitvsolo.exchangeapi.service.ExchangeService;
import com.makeitvsolo.exchangeapi.service.dto.exchange.CreateExchangeDto;
import com.makeitvsolo.exchangeapi.service.exception.currency.CurrencyNotFoundException;
import com.makeitvsolo.exchangeapi.service.exception.exchange.ExchangeAlreadyExistsException;
import com.makeitvsolo.exchangeapi.service.exception.validation.InvalidPayloadException;
import com.makeitvsolo.exchangeapi.servlet.exception.ParsePayloadException;
import com.makeitvsolo.exchangeapi.servlet.message.ErrorMessage;
import com.makeitvsolo.exchangeapi.servlet.query.ParsePayload;
import com.makeitvsolo.exchangeapi.servlet.query.exchange.ParseCreateExchange;
import com.makeitvsolo.exchangeapi.servlet.query.exchange.ParseExchangeRate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "exchanges", urlPatterns = "/exchanges")
public final class ExchangesServlet extends HttpServlet {
    private final ExchangeService service = ApplicationConfig.Services.Exchange.configured();
    private final ParsePayload<CreateExchangeDto> payload = new ParseCreateExchange();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.setStatus(HttpServletResponse.SC_OK);

            var exchanges = service.all();
            objectMapper.writeValue(resp.getWriter(), exchanges);
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

            var exchange = service.create(payload.parseFrom(req.getReader()));
            objectMapper.writeValue(resp.getWriter(), exchange);
        } catch (
                ParsePayloadException |
                InvalidPayloadException |
                InvalidCurrencyCodeException |
                WrongExchangeException e
        ) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            var message = new ErrorMessage(e.getMessage());
            objectMapper.writeValue(resp.getWriter(), message);
        } catch (CurrencyNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

            var message = new ErrorMessage(e.getMessage());
            objectMapper.writeValue(resp.getWriter(), message);
        } catch (ExchangeAlreadyExistsException e) {
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
