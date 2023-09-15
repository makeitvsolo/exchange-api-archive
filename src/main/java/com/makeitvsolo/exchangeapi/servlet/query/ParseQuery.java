package com.makeitvsolo.exchangeapi.servlet.query;

public interface ParseQuery<T> {

    T parse(String query);
}
