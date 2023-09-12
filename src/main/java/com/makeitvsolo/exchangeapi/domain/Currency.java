package com.makeitvsolo.exchangeapi.domain;

import com.makeitvsolo.exchangeapi.core.unique.Unique;
import com.makeitvsolo.exchangeapi.domain.mapping.MappedFromCurrency;

import java.util.UUID;

public sealed interface Currency {

    <R> R map(MappedFromCurrency<R> mapper);

    static Currency create(Unique<UUID> id, String code, String fullName, String sign) {
        return Currency.from(id.unique(), code, fullName, sign);
    }

    static Currency from(UUID id, String code, String fullName, String sign) {
        return new Base(id, code, fullName, sign);
    }

    final class Base implements Currency {
        private final UUID id;
        private final String code;
        private final String fullName;
        private final String sign;

        Base(UUID id, String code, String fullName, String sign) {
            this.id = id;
            this.code = code;
            this.fullName = fullName;
            this.sign = sign;
        }

        @Override
        public <R> R map(MappedFromCurrency<R> mapped) {
            return mapped.from(id, code, fullName, sign);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Base other)) {
                return false;
            }

            return id.equals(other.id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }
}
