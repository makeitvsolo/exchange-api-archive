package com.makeitvsolo.exchangeapi.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeitvsolo.exchangeapi.ApplicationConfig;
import com.makeitvsolo.exchangeapi.domain.exception.InvalidCurrencyCodeException;
import com.makeitvsolo.exchangeapi.domain.exception.WrongExchangeException;
import com.makeitvsolo.exchangeapi.service.ExchangeService;
import com.makeitvsolo.exchangeapi.service.dto.exchange.ExchangeCodeDto;
import com.makeitvsolo.exchangeapi.service.dto.exchange.UpdateExchangeDto;
import com.makeitvsolo.exchangeapi.service.exception.exchange.ExchangeNotFoundException;
import com.makeitvsolo.exchangeapi.service.exception.validation.InvalidPayloadException;
import com.makeitvsolo.exchangeapi.servlet.exception.ParsePayloadException;
import com.makeitvsolo.exchangeapi.servlet.exception.ParseQueryException;
import com.makeitvsolo.exchangeapi.servlet.message.ErrorMessage;
import com.makeitvsolo.exchangeapi.servlet.query.ParsePayload;
import com.makeitvsolo.exchangeapi.servlet.query.ParseQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "exchange", urlPatterns = "/exchanges/*")
public final class ExchangeServlet extends HttpServlet {
    private final ExchangeService service = ApplicationConfig.Services.Exchange.configured();
    private final ParseQuery<ExchangeCodeDto> query = ApplicationConfig.Parsers.Query.ToExchange.configured();
    private final ParsePayload<BigDecimal> payload = ApplicationConfig.Parsers.Payload.ToExchangeRate.configured();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.setStatus(HttpServletResponse.SC_OK);

            var payload = query.parse(req.getPathInfo().replaceAll("/", ""));
            var exchange = service.byCode(payload);

            objectMapper.writeValue(resp.getWriter(), exchange);
        } catch (ParseQueryException| InvalidPayloadException | InvalidCurrencyCodeException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            var message = new ErrorMessage(e.getMessage());
            objectMapper.writeValue(resp.getWriter(), message);
        } catch (ExchangeNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

            var message = new ErrorMessage(e.getMessage());
            objectMapper.writeValue(resp.getWriter(), message);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            var message = new ErrorMessage("Internal server error");
            objectMapper.writeValue(resp.getWriter(), message);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.setStatus(HttpServletResponse.SC_OK);

            var code = query.parse(req.getPathInfo().replaceAll("/", ""));
            var rate = payload.parseFrom(req.getReader());

            var exchange = service.update(new UpdateExchangeDto(code.base(), code.target(), rate));
            objectMapper.writeValue(resp.getWriter(), exchange);
        } catch (
                ParsePayloadException |
                ParseQueryException |
                InvalidPayloadException |
                InvalidCurrencyCodeException |
                WrongExchangeException e
        ) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            var message = new ErrorMessage(e.getMessage());
            objectMapper.writeValue(resp.getWriter(), message);
        } catch (ExchangeNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

            var message = new ErrorMessage(e.getMessage());
            objectMapper.writeValue(resp.getWriter(), message);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            var message = new ErrorMessage("Internal server error");
            objectMapper.writeValue(resp.getWriter(), message);
        }
    }
}
