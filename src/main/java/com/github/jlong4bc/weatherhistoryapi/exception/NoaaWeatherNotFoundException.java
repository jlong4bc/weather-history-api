package com.github.jlong4bc.weatherhistoryapi.exception;


/**
 * Indicates missing weather data
 */
public class NoaaWeatherNotFoundException extends RuntimeException
{
    public NoaaWeatherNotFoundException(String msg) {
        super(msg);
    }
}
