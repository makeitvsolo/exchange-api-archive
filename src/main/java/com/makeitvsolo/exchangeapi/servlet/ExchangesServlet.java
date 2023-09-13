package com.makeitvsolo.exchangeapi.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeitvsolo.exchangeapi.service.ExchangeService;
import com.makeitvsolo.exchangeapi.service.dto.exchange.CreateExchangeDto;
import com.makeitvsolo.exchangeapi.service.exception.exchange.ExchangeAlreadyExistsException;
import com.makeitvsolo.exchangeapi.servlet.error.ErrorMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "exchange rate", urlPatterns = "/exchanges")
public final class ExchangesServlet extends HttpServlet {
    private ExchangeService service;
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

            var base = req.getParameter("base");
            var target = req.getParameter("target");
            var rate = BigDecimal.valueOf(Double.parseDouble(req.getParameter("rate")));

            var payload = new CreateExchangeDto(base, target, rate);
            var exchange = service.create(payload);
            objectMapper.writeValue(resp.getWriter(), exchange);
        } catch (ExchangeAlreadyExistsException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);

            var message = new ErrorMessage(e.getMessage());
            objectMapper.writeValue(resp.getWriter(), message);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            var message = new ErrorMessage(e.getMessage());
            objectMapper.writeValue(resp.getWriter(), message);
        }
    }
}
