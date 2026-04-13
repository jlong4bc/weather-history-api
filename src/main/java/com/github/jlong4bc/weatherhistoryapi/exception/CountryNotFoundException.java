package com.github.jlong4bc.weatherhistoryapi.exception;

/**
 * Provides specific exception to communicate the input was incorrect.
 */
public class CountryNotFoundException extends RuntimeException
{
    public CountryNotFoundException() {
        super();
    }

    public CountryNotFoundException(String message) {
        super(message);
    }
}
