package com.makeitvsolo.exchangeapi.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeitvsolo.exchangeapi.service.ExchangeService;
import com.makeitvsolo.exchangeapi.service.dto.exchange.ExchangeCodeDto;
import com.makeitvsolo.exchangeapi.service.dto.exchange.UpdateExchangeDto;
import com.makeitvsolo.exchangeapi.service.exception.exchange.ExchangeNotFoundException;
import com.makeitvsolo.exchangeapi.servlet.error.ErrorMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "exchange rate", urlPatterns = "/exchange/*")
public final class ExchangeServlet extends HttpServlet {
    private ExchangeService service;
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

            var path = req.getPathInfo().replaceAll("/", "");

            var payload = new ExchangeCodeDto(path.substring(0, 3), path.substring(3));
            var exchange = service.byCode(payload);
            objectMapper.writeValue(resp.getWriter(), exchange);
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

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.setStatus(HttpServletResponse.SC_OK);

            var path = req.getPathInfo().replaceAll("/", "");
            var rate = BigDecimal.valueOf(Double.parseDouble(
                    req.getReader()
                            .readLine()
                            .replace("rate=", "")
            ));

            var payload = new UpdateExchangeDto(path.substring(0, 3), path.substring(3), rate);
            var exchange = service.update(payload);
            objectMapper.writeValue(resp.getWriter(), exchange);
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