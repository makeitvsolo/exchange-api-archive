package com.makeitvsolo.exchangeapi.domain.exchange;

import com.makeitvsolo.exchangeapi.domain.currency.Currency;

import java.math.BigDecimal;
import java.math.MathContext;

public sealed interface Exchange {

    Currency base();
    Currency target();
    BigDecimal rate();

    BigDecimal convert(BigDecimal amount);
    Exchange updated(BigDecimal rate);

    <R> R map(MappedFromExchange<R> mapped);

    static Exchange create(Currency base, Currency target, BigDecimal rate) {
        return Exchange.from(base, target, rate);
    }

    static Exchange from(Currency base, Currency target, BigDecimal rate) {
        if (base.equals(target)) {
            throw new WrongExchangeException("Exchange is only possible with different currencies");
        }

        if (rate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WrongExchangeException("Exchange rate should be `>0`");
        }

        return new Base(base, target, rate);
    }

    final class Base implements Exchange {
        private final Currency base;
        private final Currency target;
        private final BigDecimal rate;

        Base(Currency base, Currency target, BigDecimal rate) {
            this.base = base;
            this.target = target;
            this.rate = rate;
        }

        @Override
        public Currency base() {
            return base;
        }

        @Override
        public Currency target() {
            return target;
        }

        @Override
        public BigDecimal rate() {
            return rate;
        }

        @Override
        public BigDecimal convert(BigDecimal amount) {
            return amount.multiply(rate);
        }

        @Override
        public Exchange updated(BigDecimal rate) {
            return Exchange.from(base, target, rate);
        }

        @Override
        public <R> R map(MappedFromExchange<R> mapped) {
            return mapped.from(base, target, rate);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Base other)) {
                return false;
            }

            return base.equals(other.base)
                    && target.equals(other.target);
        }

        @Override
        public int hashCode() {
            return base.hashCode() ^ target.hashCode();
        }
    }

    final class Reversed implements Exchange {
        private final Exchange exchange;

        Reversed(Exchange exchange) {
            this.exchange = exchange;
        }

        public static Reversed of(Exchange exchange) {
            return new Reversed(exchange);
        }

        @Override
        public Currency base() {
            return exchange.target();
        }

        @Override
        public Currency target() {
            return exchange.base();
        }

        @Override
        public BigDecimal rate() {
            return BigDecimal.ONE.divide(exchange.rate(), MathContext.DECIMAL64);
        }

        @Override
        public BigDecimal convert(BigDecimal amount) {
            return amount.multiply(rate());
        }

        @Override
        public Exchange updated(BigDecimal rate) {
            return Exchange.from(base(), target(), rate);
        }

        @Override
        public <R> R map(MappedFromExchange<R> mapped) {
            return mapped.from(base(), target(), rate());
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Exchange other)) {
                return false;
            }

            return exchange.equals(other);
        }
    }

    final class Cross implements Exchange {
        private final Exchange first;
        private final Exchange second;

        Cross(Exchange first, Exchange second) {
            this.first = first;
            this.second = second;
        }

        public static Cross of(Exchange first, Exchange second) {
            if (first.equals(second)) {
                throw new IllegalStateException();
            }

            if (!first.base().equals(second.base())) {
                throw new IllegalStateException();
            }

            return new Cross(first, second);
        }

        @Override
        public Currency base() {
            return first.target();
        }

        @Override
        public Currency target() {
            return second.target();
        }

        @Override
        public BigDecimal rate() {
            var firstRate = Reversed.of(first).rate();

            return firstRate.multiply(second.rate());
        }

        @Override
        public BigDecimal convert(BigDecimal amount) {
            return second.convert(rate());
        }

        @Override
        public Exchange updated(BigDecimal rate) {
            return Exchange.from(base(), target(), rate);
        }

        @Override
        public <R> R map(MappedFromExchange<R> mapped) {
            return mapped.from(base(), target(), rate());
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Exchange other)) {
                return false;
            }

            return base().equals(other.base())
                    && target().equals(other.target());
        }

        @Override
        public int hashCode() {
            return first.hashCode() ^ second.hashCode();
        }
    }
}
