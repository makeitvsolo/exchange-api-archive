package com.makeitvsolo.exchangeapi.domain;

import com.makeitvsolo.exchangeapi.domain.exception.InvalidCurrencyCodeException;
import com.makeitvsolo.exchangeapi.domain.mapping.MappedFromCurrency;

public sealed interface Currency {

    <R> R map(MappedFromCurrency<R> mapper);

    static Currency create(String code, String fullName, String sign) {
        return Currency.from(code, fullName, sign);
    }

    static Currency from(String code, String fullName, String sign) {
        java.util.Currency.getAvailableCurrencies()
                .stream()
                .map(java.util.Currency::getCurrencyCode)
                .filter(correct -> correct.equals(code))
                .findFirst()
                .orElseThrow(() -> new InvalidCurrencyCodeException(code));

        return new Base(code, fullName, sign);
    }

    final class Base implements Currency {
        private final String code;
        private final String fullName;
        private final String sign;

        Base(String code, String fullName, String sign) {
            this.code = code;
            this.fullName = fullName;
            this.sign = sign;
        }

        @Override
        public <R> R map(MappedFromCurrency<R> mapped) {
            return mapped.from(code, fullName, sign);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Base other)) {
                return false;
            }

            return code.equals(other.code);
        }

        @Override
        public int hashCode() {
            return code.hashCode();
        }
    }
}
