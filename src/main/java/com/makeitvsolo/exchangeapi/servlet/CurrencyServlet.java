package com.makeitvsolo.exchangeapi.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeitvsolo.exchangeapi.ApplicationConfig;
import com.makeitvsolo.exchangeapi.domain.exception.InvalidCurrencyCodeException;
import com.makeitvsolo.exchangeapi.service.CurrencyService;
import com.makeitvsolo.exchangeapi.service.exception.currency.CurrencyNotFoundException;
import com.makeitvsolo.exchangeapi.service.exception.validation.InvalidPayloadException;
import com.makeitvsolo.exchangeapi.servlet.exception.ParseQueryException;
import com.makeitvsolo.exchangeapi.servlet.message.ErrorMessage;
import com.makeitvsolo.exchangeapi.servlet.query.ParseQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "currency", urlPatterns = "/currencies/*")
public final class CurrencyServlet extends HttpServlet {
    private final CurrencyService service = ApplicationConfig.Services.Currency.configured();
    private final ParseQuery<String> query = ApplicationConfig.Parsers.Query.ToCurrencyCode.configured();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.setStatus(HttpServletResponse.SC_OK);

            var payload = query.parse(req.getPathInfo().replaceAll("/", ""));

            var currency = service.byCode(payload);
            objectMapper.writeValue(resp.getWriter(), currency);
        } catch (ParseQueryException | InvalidPayloadException | InvalidCurrencyCodeException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            var message = new ErrorMessage(e.getMessage());
            objectMapper.writeValue(resp.getWriter(), message);
        } catch (CurrencyNotFoundException e) {
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
