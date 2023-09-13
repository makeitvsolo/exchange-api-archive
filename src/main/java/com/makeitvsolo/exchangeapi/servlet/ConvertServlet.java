package com.makeitvsolo.exchangeapi.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeitvsolo.exchangeapi.service.ConvertService;
import com.makeitvsolo.exchangeapi.service.dto.convert.ConvertAmountDto;
import com.makeitvsolo.exchangeapi.service.exception.exchange.ExchangeNotFoundException;
import com.makeitvsolo.exchangeapi.servlet.error.ErrorMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "convert", urlPatterns = "/convert")
public final class ConvertServlet extends HttpServlet {
    private ConvertService service;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.setStatus(HttpServletResponse.SC_OK);

            var base = req.getParameter("from");
            var target = req.getParameter("to");
            var amount = BigDecimal.valueOf(Double.parseDouble(req.getParameter("amount")));

            var payload = new ConvertAmountDto(base, target, amount);
            var converted = service.convert(payload);
            objectMapper.writeValue(resp.getWriter(), converted);
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
