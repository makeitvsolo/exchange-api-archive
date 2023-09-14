package com.makeitvsolo.exchangeapi.core.unique;

import java.util.UUID;

public final class UniqueUUID implements Unique<UUID> {

    public UniqueUUID() {
    }

    @Override
    public UUID unique() {
        return UUID.randomUUID();
    }
}
