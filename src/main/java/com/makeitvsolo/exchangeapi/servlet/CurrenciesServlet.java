package com.makeitvsolo.exchangeapi.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeitvsolo.exchangeapi.service.CurrencyService;
import com.makeitvsolo.exchangeapi.service.dto.currency.CreateCurrencyDto;
import com.makeitvsolo.exchangeapi.service.exception.currency.CurrencyAlreadyExistsException;
import com.makeitvsolo.exchangeapi.servlet.error.ErrorMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "currencies", urlPatterns = "/currencies")
public final class CurrenciesServlet extends HttpServlet {
    private CurrencyService service;
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

            String code = req.getParameter("code");
            String fullName = req.getParameter("full_name");
            String sign = req.getParameter("sign");

            var payload = new CreateCurrencyDto(code, fullName, sign);
            var currency = service.create(payload);

            objectMapper.writeValue(resp.getWriter(), currency);
        } catch (CurrencyAlreadyExistsException e) {
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
