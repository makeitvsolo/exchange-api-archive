package com.makeitvsolo.exchangeapi.domain;

import com.makeitvsolo.exchangeapi.domain.mapping.MappedFromExchange;
import com.makeitvsolo.exchangeapi.domain.exception.WrongExchangeException;

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

    sealed interface Cross extends Exchange {

        static Cross of(Exchange first, Exchange second) {
            if (first.equals(second)) {
                throw new WrongExchangeException("Cross exchange can't be with the same exchanges");
            }

            if (first.base().equals(second.base())) {
                return new Left(first, second);
            }

            if (first.target().equals(second.target())) {
                return new Right(first, second);
            }

            throw new WrongExchangeException("Cross exchange impossible");
        }

        final class Left implements Cross {
            private final Exchange first;
            private final Exchange second;

            Left(Exchange first, Exchange second) {
                this.first = first;
                this.second = second;
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
                var firstReversed = Reversed.of(first);

                return second.convert(firstReversed.convert(amount));
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

        final class Right implements Cross {
            private final Exchange first;
            private final Exchange second;

            Right(Exchange first, Exchange second) {
                this.first = first;
                this.second = second;
            }

            @Override
            public Currency base() {
                return first.base();
            }

            @Override
            public Currency target() {
                return second.base();
            }

            @Override
            public BigDecimal rate() {
                var secondRate = Reversed.of(second).rate();

                return first.rate().multiply(secondRate);
            }

            @Override
            public BigDecimal convert(BigDecimal amount) {
                var secondReversed = Reversed.of(second);

                return secondReversed.convert(first.convert(amount));
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
}
