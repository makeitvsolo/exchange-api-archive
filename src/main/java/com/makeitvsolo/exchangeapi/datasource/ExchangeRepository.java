package com.makeitvsolo.exchangeapi.datasource;

import com.makeitvsolo.exchangeapi.domain.Exchange;
import org.javatuples.Pair;

import java.util.List;
import java.util.Optional;

public interface ExchangeRepository {

    void save(Exchange exchange);
    void update(Exchange exchange);

    Optional<Exchange> fetchByCode(String base, String target);
    List<Exchange> fetchAll();

    Optional<Pair<Exchange, Exchange>> fetchAnyCrossPair(String base, String target);
}
