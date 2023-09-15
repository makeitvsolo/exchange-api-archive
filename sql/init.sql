CREATE TABLE IF NOT EXISTS currencies (
    code VARCHAR(3),
    full_name VARCHAR NOT NULL,
    sign VARCHAR(5) NOT NULL,

    PRIMARY KEY (code)
);

CREATE TABLE IF NOT EXISTS exchanges (
    base_currency_code VARCHAR(3),
    target_currency_code VARCHAR(3),
    rate DECIMAL NOT NULL,

    PRIMARY KEY(base_currency_code, target_currency_code),
    FOREIGN KEY(base_currency_code) REFERENCES currencies(code) ON DELETE CASCADE,
    FOREIGN KEY(target_currency_code) REFERENCES currencies(code) ON DELETE CASCADE
);
