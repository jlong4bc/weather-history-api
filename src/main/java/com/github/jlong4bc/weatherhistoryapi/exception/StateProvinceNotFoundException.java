package com.github.jlong4bc.weatherhistoryapi.exception;


/**
 * Provides specific exception to communicate the input was incorrect.
 */
public class StateProvinceNotFoundException extends RuntimeException
{
    public StateProvinceNotFoundException() {
        super();
    }

    public StateProvinceNotFoundException(String message) {
        super(message);
    }
}
