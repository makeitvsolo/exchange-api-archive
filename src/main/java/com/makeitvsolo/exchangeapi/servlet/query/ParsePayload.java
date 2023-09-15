package com.makeitvsolo.exchangeapi.servlet.query;

import java.io.BufferedReader;

public interface ParsePayload<T> {

    T parseFrom(BufferedReader reader);
}
