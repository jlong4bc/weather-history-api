package com.github.jlong4bc.weatherhistoryapi.exception;

/**
 * An exception to throw when a NOAA weather station cannot be found.
 */
public class NoaaStationNotFoundException extends RuntimeException
{
    public NoaaStationNotFoundException(String message) {
        super(message);
    }
}
